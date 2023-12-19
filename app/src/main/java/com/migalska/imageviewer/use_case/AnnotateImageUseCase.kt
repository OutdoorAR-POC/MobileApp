package com.migalska.imageviewer.use_case

import android.app.Application
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.migalska.imageviewer.data.entity.CameraIntrinsic
import com.migalska.imageviewer.data.entity.CameraPosition
import com.migalska.imageviewer.data.entity.Image
import com.migalska.imageviewer.data.entity.PolylineMetadata
import com.migalska.imageviewer.data.json.Polyline
import com.migalska.imageviewer.data.json.Vertex
import com.migalska.imageviewer.data.repository.CameraIntrinsicRepository
import com.migalska.imageviewer.data.repository.CameraPositionRepository
import com.migalska.imageviewer.data.repository.ImageRepository
import com.migalska.imageviewer.data.repository.PolylineMetadataRepository
import com.migalska.imageviewer.viewmodel.AnnotatedImage
import com.migalska.imageviewer.viewmodel.StyledPath
import com.migalska.imageviewer.util.COLUMN_ORDER
import com.migalska.imageviewer.util.reshape
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.util.FastMath
import java.io.BufferedReader
import javax.inject.Inject
import kotlin.math.PI
import kotlin.math.floor

class AnnotateImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    private val intrinsicRepository: CameraIntrinsicRepository,
    private val positionRepository: CameraPositionRepository,
    private val polylineMetadataRepository: PolylineMetadataRepository,
    private val application: Application
) {

    suspend operator fun invoke(
        imageFileName: String,
    ): AnnotatedImage {
        val bitmap =
            application.assets.open("capturedImages/$imageFileName").use {
                BitmapFactory.decodeStream(it).asImageBitmap()
            }
        val polylinesMetadata = polylineMetadataRepository.getPolylinesInNeighbourhood(
            0.0, 0.0, 0.0, n = 16
        )
        val polylines = parsePolylines(polylinesMetadata)
        val colors = parsePolylinesColors(polylinesMetadata)
        val image = imageRepository.getByName(imageFileName) ?: return AnnotatedImage(bitmap)

        val intrinsic = intrinsicRepository.getById(image.intrinsicId)
        val pose = positionRepository.getById(image.poseId)
        if (intrinsic == null || pose == null) {
            return AnnotatedImage(bitmap)
        }

        val paths = calculate(
            pose,
            intrinsic,
            image,
            polylines,
            colors,
        )
        return AnnotatedImage(bitmap, paths)
    }


    private fun getColor(name: String): Color {
        val tokens = name.split("/")
        val color = tokens[tokens.size - 1].substringBefore("Polyline.json").lowercase()
        return when (color) {
            "blue" -> Color.Blue
            "red" -> Color.Red
            "yellow" -> Color.Yellow
            "green" -> Color.Green
            else -> Color.Unspecified
        }
    }

    private fun parsePolylinesColors(polylinesMetadata: List<PolylineMetadata>): List<Color> {
        return polylinesMetadata.map { getColor(it.path) }
    }

    private fun parsePolylines(polylinesMetadata: List<PolylineMetadata>): List<Polyline> {
        return polylinesMetadata.map { parsePolyline(it.path) }
    }

    private fun parsePolyline(name: String): Polyline {
        application.assets.open(name).bufferedReader().use { reader ->
            return parsePolyline(reader)
        }
    }

    private fun calculate(
        pose: CameraPosition,
        intrinsic: CameraIntrinsic,
        image: Image,
        polylines: List<Polyline>,
        colors: List<Color>,
    ): List<StyledPath> {

        val rotation: RealMatrix = MatrixUtils.createRealMatrix(arrayOf(pose.rotationMatrix))
            .reshape(3, 3, order = COLUMN_ORDER)
        val center: RealMatrix = MatrixUtils.createColumnRealMatrix(pose.center)
        val translation: RealMatrix = rotation.multiply(center).scalarMultiply((-1.0))

        val extrinsicMatrix: RealMatrix = MatrixUtils.createRealMatrix(4, 4).apply {
            setSubMatrix(rotation.data, 0, 0)
            setSubMatrix(translation.data, 0, 3)
            setEntry(3, 3, 1.0)
        }
        val imageWidth = image.width
        val imageHeight = image.height

        val intrinsicMatrix: RealMatrix = intrinsic.getIntrinsicMatrix()

        val coloredPaths = mutableListOf<StyledPath>()
        val dashed = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))

        polylines.zip(colors).forEach { (polyline, color) ->
            val imageCoordinates = getImageCoordinates(
                MatrixUtils.createRealMatrix(polyline.coordinates),
                intrinsicMatrix,
                extrinsicMatrix,
            )
            val visibility = isVisible(
                imageCoordinates,
                imageWidth,
                imageHeight,
                polyline.vertices,
                pose.center
            )
            val anyVisible = visibility.reduce { acc, b -> acc || b }
            if (anyVisible) {
                val widthFactor = 1132f / imageWidth.toFloat() //TODO: this cannot be static, perhaps draw points instead of paths?
                val heightFactor = 849f / imageHeight.toFloat()  //TODO: this cannot be static
                for (edge in polyline.edges) {
                    val v1Visible = visibility[edge.vertex1]
                    val v2Visible = visibility[edge.vertex2]
                    if (v1Visible || v2Visible) {
                        val x1 = FastMath.max(
                            FastMath.min(
                                imageCoordinates.data[0][edge.vertex1].toFloat(),
                                imageWidth.toFloat()
                            ), 0f
                        ) * widthFactor
                        val y1 = FastMath.max(
                            FastMath.min(
                                imageCoordinates.data[1][edge.vertex1].toFloat(),
                                imageHeight.toFloat()
                            ), 0f
                        ) * heightFactor
                        val x2 = FastMath.max(
                            FastMath.min(
                                imageCoordinates.data[0][edge.vertex2].toFloat(),
                                imageWidth.toFloat()
                            ), 0f
                        ) * widthFactor
                        val y2 = FastMath.max(
                            FastMath.min(
                                imageCoordinates.data[1][edge.vertex2].toFloat(),
                                imageHeight.toFloat()
                            ), 0f
                        ) * heightFactor
                        val path = Path()
                        path.moveTo(x1, y1)
                        path.lineTo(x2, y2)
                        coloredPaths.add(
                            StyledPath(
                                path,
                                color,
                                if (v1Visible && v2Visible) null else dashed,
                            )
                        )
                    }

                }
            }
        }

        return coloredPaths

    }

    companion object {

        fun parsePolyline(reader: BufferedReader): Polyline {
            val mapper = jacksonObjectMapper()
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            mapper.enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature())
            return mapper.readValue(reader)
        }

        data class SphericalCoordinates(val u: DoubleArray, val v: DoubleArray) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as SphericalCoordinates

                if (!u.contentEquals(other.u)) return false
                if (!v.contentEquals(other.v)) return false

                return true
            }

            override fun hashCode(): Int {
                var result = u.contentHashCode()
                result = 31 * result + v.contentHashCode()
                return result
            }
        }

        fun getDelta(vertex: Vertex, eye: DoubleArray): DoubleArray {
            return doubleArrayOf(eye[0] - vertex.x, eye[1] - vertex.y, eye[2] - vertex.z)
        }

        fun getEuclideanDistance(delta: DoubleArray): Double {
            return FastMath.sqrt(delta.map { elem -> elem * elem }
                .reduce { acc, next -> acc + next })
        }

        fun getPolarAngle(delta: DoubleArray, r: Double): Double {
            return FastMath.acos(delta[2] / r)
        }

        fun getAzimuthalAngle(delta: DoubleArray): Double {
            return (FastMath.atan2(delta[1], delta[0])).mod(2 * PI)
        }

        fun getVertexVisibilityIndex(
            n: Int,
            delta: DoubleArray,
            distance: Double,
        ): Int {
            val polarIntervalLength = PI / n
            val azimuthalIntervalLength = 2*PI/n
            val polarAngle = getPolarAngle(delta, distance)
            val azimuthalAngle = getAzimuthalAngle(delta)
            val azimuthalIdx = floor(azimuthalAngle/azimuthalIntervalLength).toInt()
            val polarIdx = floor(polarAngle/polarIntervalLength).toInt()
            return polarIdx * n + azimuthalIdx
        }

        fun calculateVertexVisibility(
            vertex: Vertex,
            eye: DoubleArray,
            n: Int,
        ): Boolean {
            val delta = getDelta(vertex, eye)
            val distance = getEuclideanDistance(delta)
            val vertexVisibilityIdx: Int = getVertexVisibilityIndex(n, delta, distance)
            return vertex.visibility_grid[vertexVisibilityIdx] >= distance
        }

        fun isInside(x: Double, y: Double, imageWidth: Int, imageHeight: Int): Boolean {
            return (x >= 0) && (x <= imageWidth) && (y >= 0) && (y <= imageHeight)
        }

        fun isVisible(
            imageCoordinates: RealMatrix,
            imageWidth: Int,
            imageHeight: Int,
            vertices: List<Vertex>,
            eye: DoubleArray,
        ): List<Boolean> {
            val n = FastMath.sqrt(vertices[0].visibility_grid.size.toDouble()).toInt()
            return vertices.withIndex().map { vertex ->
                isVisible(
                    imageCoordinates.data[0][vertex.index],
                    imageCoordinates.data[1][vertex.index],
                    imageWidth,
                    imageHeight,
                    vertex.value,
                    eye,
                    n,
                )
            }
        }

        private fun isVisible(
            x: Double,
            y: Double,
            imageWidth: Int,
            imageHeight: Int,
            vertex: Vertex,
            eye: DoubleArray,
            n: Int,
        ): Boolean {
            return isInside(x, y, imageWidth, imageHeight)
                    && calculateVertexVisibility(vertex, eye, n)
        }

        fun getSphericalCoordinates(n: Int): SphericalCoordinates {
            if (n <= 0) {
                throw Exception("Number of spherical coordinates must be positive")
            }
            val uOffset = 2 * PI / (2 * n)
            val vOffset = PI / (2 * n)
            val u = linspace(uOffset, 2 * PI - uOffset, n)
            val v = linspace(vOffset, PI - vOffset, n)
            return SphericalCoordinates(u, v)
        }

        fun linspace(start: Double, end: Double, num: Int): DoubleArray {
            val step = (end - start) / (num - 1)
            return (0 until num).map { i -> start + (step * i) }.toDoubleArray()
        }

        fun getImageCoordinates(polyline: RealMatrix, K: RealMatrix, M: RealMatrix): RealMatrix {

            val polyline4d = mutableListOf<DoubleArray>()
            for (row in polyline.data) {
                val newRow: MutableList<Double> = row.toMutableList()
                newRow.add(1.0)
                polyline4d.add(newRow.toDoubleArray())
            }
            val polyline4DRM: RealMatrix = MatrixUtils.createRealMatrix(polyline4d.toTypedArray())
            val points = K.multiply(M.multiply(polyline4DRM.transpose()))

            val lastRow = points.getRow(points.rowDimension - 1)
            val result = mutableListOf<DoubleArray>()
            for (row in points.data) {
                val newRow: MutableList<Double> = row.toMutableList()
                for (i in 0 until newRow.size) {
                    newRow[i] = FastMath.round(newRow[i] / lastRow[i]).toDouble()
                }
                result.add(newRow.toDoubleArray())
            }

            return MatrixUtils.createRealMatrix(result.toTypedArray())
        }

    }
}