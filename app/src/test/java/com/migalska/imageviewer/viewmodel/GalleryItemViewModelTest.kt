package com.migalska.imageviewer.viewmodel

import com.migalska.imageviewer.assertArrayAlmostEquals
import com.migalska.imageviewer.data.entity.CameraPosition
import com.migalska.imageviewer.data.json.Vertex
import com.migalska.imageviewer.use_case.AnnotateImageUseCase
import com.migalska.imageviewer.use_case.AnnotateImageUseCase.Companion.getAzimuthalAngle
import com.migalska.imageviewer.use_case.AnnotateImageUseCase.Companion.getDelta
import com.migalska.imageviewer.use_case.AnnotateImageUseCase.Companion.getEuclideanDistance
import com.migalska.imageviewer.use_case.AnnotateImageUseCase.Companion.getImageCoordinates
import com.migalska.imageviewer.use_case.AnnotateImageUseCase.Companion.getPolarAngle
import com.migalska.imageviewer.use_case.AnnotateImageUseCase.Companion.getSphericalCoordinates
import com.migalska.imageviewer.use_case.AnnotateImageUseCase.Companion.getVertexVisibilityIndex
import com.migalska.imageviewer.use_case.AnnotateImageUseCase.Companion.isInside
import com.migalska.imageviewer.util.COLUMN_ORDER
import com.migalska.imageviewer.util.reshape
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.File
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.sqrt


internal class GalleryItemViewModelTest {

    @Test
    fun testGetImageCoordinates() {

        val redPolyline = MatrixUtils.createRealMatrix(
            arrayOf(
                doubleArrayOf(1.359150052071E+00, -4.094719886780E-01, 1.373669981956E+00),
                doubleArrayOf(1.052219986916E+00, -1.273890025914E-02, 1.117300033569E+00),
                doubleArrayOf(7.79443E-01, 1.24142E-01, 1.194797E+00),
                doubleArrayOf(4.97258E-01, 1.89861E-01, 1.46525E+00),
                doubleArrayOf(5.874119997025E-01, 4.343109950423E-02, 1.556470036507E+00),
                doubleArrayOf(6.33452E-01, 1.4838E-02, 1.501833E+00),
                doubleArrayOf(7.467389702797E-01, -8.026939630508E-02, 1.528660058975E+00),
                doubleArrayOf(9.68015E-01, -1.91973E-01, 1.548352E+00),
                doubleArrayOf(1.106259942055E+00, -4.076380133629E-01, 1.612190008163E+00),
                doubleArrayOf(1.359150052071E+00, -4.094719886780E-01, 1.373669981956E+00)
            )
        )

        val intrinsic = MatrixUtils.createRealMatrix(
            arrayOf(
                doubleArrayOf(3.00185248e+03, 0.00000000e+00, 2.00053931e+03, 0.00000000e+00),
                doubleArrayOf(0.00000000e+00, 3.00185248e+03, 1.49549277e+03, 0.00000000e+00),
                doubleArrayOf(0.00000000e+00, 0.00000000e+00, 1.00000000e+00, 0.00000000e+00)
            )
        )
        val rotationTranslation = MatrixUtils.createRealMatrix(
            arrayOf(
                doubleArrayOf(-2.77263608e-01, -7.38016448e-02, -9.57955223e-01, 2.87337298e+00),
                doubleArrayOf(2.45759923e-01, -9.69324240e-01, 3.54655432e-03, -5.56134122e-02),
                doubleArrayOf(-9.28830960e-01, -2.34443671e-01, 2.86895823e-01, 5.67559977e+00),
                doubleArrayOf(0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 1.00000000e+00)
            )
        )
        val expectedResult = MatrixUtils.createRealMatrix(
            arrayOf(
                doubleArrayOf(
                    2742.0,
                    2905.0,
                    2858.0,
                    2708.0,
                    2656.0,
                    2685.0,
                    2667.0,
                    2648.0,
                    2607.0,
                    2742.0
                ),
                doubleArrayOf(
                    1912.0,
                    1627.0,
                    1507.0,
                    1435.0,
                    1524.0,
                    1545.0,
                    1612.0,
                    1709.0,
                    1851.0,
                    1912.0
                ),
                doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
            )
        )
        val result =
            getImageCoordinates(redPolyline, intrinsic, rotationTranslation)
        assertEquals("results are equal", result, expectedResult)
    }

    @Test
    fun testCalculate() {
        val pose = CameraPosition(
            id = 647009564,
            rotationMatrix = doubleArrayOf(
                -0.2772636080936643,
                0.24575992301783944,
                -0.9288309597903962,
                -0.07380164476045274,
                -0.9693242399686033,
                -0.23444367135826305,
                -0.9579552227831606,
                0.003546554321443879,
                0.2868958227212626,
            ),
            center = doubleArrayOf(6.08202209269405, 1.4887606714272859, 1.124454019938587)
        )


        val rotation: RealMatrix = MatrixUtils.createRealMatrix(arrayOf(pose.rotationMatrix))
            .reshape(3, 3, order = COLUMN_ORDER)
        val expectedRotation = listOf(
            doubleArrayOf(-0.27726361, -0.07380164, -0.95795522),
            doubleArrayOf(0.24575992, -0.96932424, 0.00354655),
            doubleArrayOf(-0.92883096, -0.23444367, 0.28689582),
        )
        for ((actualRow, expectedRow) in rotation.data.zip(expectedRotation)) {
            assertArrayAlmostEquals(expectedRow, actualRow)
        }

        val center: RealMatrix = MatrixUtils.createColumnRealMatrix(pose.center)
        val expectedCenter = listOf(
            doubleArrayOf(6.08202209), doubleArrayOf(1.48876067), doubleArrayOf(1.12445402)
        )
        for ((actualRow, expectedRow) in center.data.zip(expectedCenter)) {
            assertArrayAlmostEquals(expectedRow, actualRow)
        }
        val translation: RealMatrix = rotation.multiply(center).scalarMultiply((-1.0))
        val expectedTranslation = listOf(
            doubleArrayOf(2.87337298),
            doubleArrayOf(-0.05561341),
            doubleArrayOf(5.67559977)
        )
        for ((actualRow, expectedRow) in translation.data.zip(expectedTranslation)) {
            assertArrayAlmostEquals(expectedRow, actualRow)
        }
        val extrinsicMatrix: RealMatrix = MatrixUtils.createRealMatrix(4, 4).apply {
            setSubMatrix(rotation.data, 0, 0)
            setSubMatrix(translation.data, 0, 3)
            setEntry(3, 3, 1.0)
        }
        val expectedExtrinsicMatrix = listOf(
            doubleArrayOf(-2.77263608e-01, -7.38016448e-02, -9.57955223e-01, 2.87337298e+00),
            doubleArrayOf(2.45759923e-01, -9.69324240e-01, 3.54655432e-03, -5.56134122e-02),
            doubleArrayOf(-9.28830960e-01, -2.34443671e-01, 2.86895823e-01, 5.67559977e+00),
            doubleArrayOf(0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 1.00000000e+00)
        )
        for ((actualRow, expectedRow) in extrinsicMatrix.data.zip(expectedExtrinsicMatrix)) {
            assertArrayAlmostEquals(expectedRow, actualRow)
        }
    }

    @Test
    fun testIsInside() {
        val coordinates = MatrixUtils.createRealMatrix(
            arrayOf(
                doubleArrayOf(
                    2742.0,
                    -2905.0,
                    2858.0,
                    2708.0,
                    2656.0,
                    2685.0,
                    2667.0,
                    2648.0,
                    2607.0,
                    2742.0
                ),
                doubleArrayOf(
                    1912.0,
                    1627.0,
                    1507.0,
                    1435.0,
                    1524.0,
                    1545.0,
                    1612.0,
                    1709.0,
                    1851.0,
                    1912.0
                ),
                doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
            )
        )
        val imageWidth = 2700
        val imageHeight = 1800
        // x: F, F, F, F, T, T, T, T, T, F
        // y: F, T, T, T, T, T, T, T, F, F
        // result
        // r: F, F, F, F, T, T, T, T, F, F
        val expectedResult = listOf(
            false, false, false, false, true, true, true, true, false, false
        )
        expectedResult.withIndex().forEach {
            assertEquals(
                "results are equal",
                it.value,
                isInside(
                    coordinates.data[0][it.index],
                    coordinates.data[1][it.index],
                    imageWidth,
                    imageHeight,
                )
            )
        }
    }

    @Test
    fun testPolylineVisibilityIdx() {
        val n = 16
        val deltas: List<DoubleArray> = arrayOf(
            doubleArrayOf(4.72287204, 1.89823266, -0.24921596),
            doubleArrayOf(5.02980211, 1.50149957, 0.00715399),
            doubleArrayOf(5.30257909, 1.36461867, -0.07034298),
            doubleArrayOf(5.58476409, 1.29889967, -0.34079598),
            doubleArrayOf(5.49461009, 1.44532957, -0.43201602),
            doubleArrayOf(5.44857009, 1.47392267, -0.37737898),
            doubleArrayOf(5.33528312, 1.56903007, -0.40420604),
            doubleArrayOf(5.11400709, 1.68073367, -0.42389798),
            doubleArrayOf(4.97576215, 1.89639868, -0.48773599),
            doubleArrayOf(4.72287204, 1.89823266, -0.24921596),
        ).toList()
        val distances: List<Double> = listOf(
            5.09616681, 5.24913911, 5.47580837, 5.74394222, 5.69792554,
            5.65701149, 5.57588413, 5.39977997, 5.34718836, 5.09616681,
        )
        val expectedResults = intArrayOf(128, 112, 128, 128, 128, 128, 128, 128, 128, 128)
        expectedResults.withIndex().forEach {
            val result = getVertexVisibilityIndex(n, deltas[it.index], distances[it.index])
            assertEquals(it.value, result)
        }
    }

    @Test
    fun testGetRootedSumOfSquares() {
        val deltas: List<DoubleArray> = listOf(
            doubleArrayOf(4.72287204, 1.89823266, -0.24921596),
            doubleArrayOf(5.02980211, 1.50149957, 0.00715399),
            doubleArrayOf(5.30257909, 1.36461867, -0.07034298),
            doubleArrayOf(5.58476409, 1.29889967, -0.34079598),
            doubleArrayOf(5.49461009, 1.44532957, -0.43201602),
            doubleArrayOf(5.44857009, 1.47392267, -0.37737898),
            doubleArrayOf(5.33528312, 1.56903007, -0.40420604),
            doubleArrayOf(5.11400709, 1.68073367, -0.42389798),
            doubleArrayOf(4.97576215, 1.89639868, -0.48773599),
            doubleArrayOf(4.72287204, 1.89823266, -0.24921596),
        ).toList()


        val expectedResults: List<Double> = listOf(
            5.09616681, 5.24913911, 5.47580837, 5.74394222, 5.69792554,
            5.65701149, 5.57588413, 5.39977997, 5.34718836, 5.09616681,
        )
        expectedResults.zip(deltas).forEach { (expectedResult, delta) ->
            val result = getEuclideanDistance(delta)
            assertEquals(expectedResult, result, 10e-8)
        }
    }

    @Test
    fun testCalculateVisibility() {
        val polyline = File("src\\main\\assets\\visibility\\n_8\\RedPolyline.json")
            .bufferedReader()
            .use { AnnotateImageUseCase.parsePolyline(it) }

        assertNotNull("polyline should not be null", polyline)
        assertEquals(10, polyline.vertices.size)
        assertEquals(64, polyline.vertices[0].visibility_grid.size)

        val eye = doubleArrayOf(-0.9753825606460071, 0.001356791348595879, 0.16693061365597311)
        val n = 8
        val vertex = polyline.vertices[3]
        val delta = doubleArrayOf(eye[0] - vertex.x, eye[1] - vertex.y, eye[2] - vertex.z)
        val distance =
            sqrt(delta.map { elem -> elem * elem }.reduce { acc, next -> acc + next })
        val polarAngle = getPolarAngle(delta, distance)
        val azimuthalAngle = getAzimuthalAngle(delta)

        val error = 0.0000001
        assertEquals(3.26890455, azimuthalAngle, error)
        assertEquals(2.28933831, polarAngle, error)

        val polarIntervalLength = PI / n
        val azimuthalIntervalLength = 2*PI/n
        val azimuthalIdx2 = floor(azimuthalAngle/azimuthalIntervalLength).toInt()
        val polarIdx2 = floor(polarAngle/polarIntervalLength).toInt()

        assertEquals(5, polarIdx2)
        assertEquals(4, azimuthalIdx2)

        val expectedResults = listOf(true, true, false, true, true, true, true, true, true, true)
        polyline.vertices.zip(expectedResults).forEach { (vertex, expectedResult) ->
            val result = AnnotateImageUseCase.calculateVertexVisibility(vertex, eye, n)
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun testModulo() {
        val error = 0.0000001
        assertEquals(3.26890455, (-3.0142807581800146).mod(2 * PI), error)
    }

    @Test
    fun testGetDelta() {
        val vertices: List<Vertex> = listOf(
            Vertex(0, 1.359150052071, -0.409471988678, 1.373669981956),
            Vertex(1, 1.052219986916, -0.01273890025914, 1.117300033569),
            Vertex(2, 0.779443, 0.124142, 1.194797),
            Vertex(3, 0.497258, 0.189861, 1.46525),
            Vertex(4, 0.5874119997025, 0.04343109950423, 1.556470036507),
            Vertex(5, 0.633452, 0.014838, 1.501833),
            Vertex(6, 0.7467389702797, -0.08026939630508, 1.528660058975),
            Vertex(7, 0.968015, -0.191973, 1.548352),
            Vertex(8, 1.106259942055, -0.4076380133629, 1.612190008163),
            Vertex(9, 1.359150052071, -0.409471988678, 1.373669981956),
        )
        val eye = doubleArrayOf(6.08202209269405, 1.4887606714272859, 1.124454019938587)
        val expectedResult = arrayOf(
            doubleArrayOf(4.72287204062305, 1.898232660105286, -0.24921596201741303),
            doubleArrayOf(5.0298021057780495, 1.501499571686426, 0.007153986369587084),
            doubleArrayOf(5.30257909269405, 1.3646186714272859, -0.07034298006141304),
            doubleArrayOf(5.58476409269405, 1.2988996714272858, -0.3407959800614129),
            doubleArrayOf(5.49461009299155, 1.445329571923056, -0.43201601656841304),
            doubleArrayOf(5.44857009269405, 1.473922671427286, -0.3773789800614129),
            doubleArrayOf(5.33528312241435, 1.5690300677323659, -0.40420603903641283),
            doubleArrayOf(5.11400709269405, 1.6807336714272858, -0.4238979800614129),
            doubleArrayOf(4.97576215063905, 1.896398684790186, -0.487735988224413),
            doubleArrayOf(4.72287204062305, 1.898232660105286, -0.24921596201741303),
        )
        vertices.zip(expectedResult).forEach { (vertex, result) ->
            assertArrayAlmostEquals(result, getDelta(vertex, eye))
        }
    }

    @Test
    fun testGetPolarAngle() {
        val deltas: List<DoubleArray> = listOf(
            doubleArrayOf(4.72287204062305, 1.898232660105286, -0.24921596201741303),
            doubleArrayOf(5.0298021057780495, 1.501499571686426, 0.007153986369587084),
            doubleArrayOf(5.30257909269405, 1.3646186714272859, -0.07034298006141304),
            doubleArrayOf(5.58476409269405, 1.2988996714272858, -0.3407959800614129),
            doubleArrayOf(5.49461009299155, 1.445329571923056, -0.43201601656841304),
            doubleArrayOf(5.44857009269405, 1.473922671427286, -0.3773789800614129),
            doubleArrayOf(5.33528312241435, 1.5690300677323659, -0.40420603903641283),
            doubleArrayOf(5.11400709269405, 1.6807336714272858, -0.4238979800614129),
            doubleArrayOf(4.97576215063905, 1.896398684790186, -0.487735988224413),
            doubleArrayOf(4.72287204062305, 1.898232660105286, -0.24921596201741303),
        )
        val rsse: List<Double> = listOf(
            5.09616681, 5.24913911, 5.47580837, 5.74394222, 5.69792554,
            5.65701149, 5.57588413, 5.39977997, 5.34718836, 5.09616681,
        )

        val expectedResults: List<Double> = listOf(
            1.61971847, 1.56943344, 1.58364282, 1.63016257, 1.64668904,
            1.63755586, 1.6433518, 1.64938001, 1.66213683, 1.61971847,
        )
        expectedResults.withIndex().forEach {
            assertEquals(it.value, getPolarAngle(deltas[it.index], rsse[it.index]), 10e-8)
        }
    }

    @Test
    fun testGetAzimuthalAngle() {
        val deltas: List<DoubleArray> = listOf(
            doubleArrayOf(4.72287204062305, 1.898232660105286, -0.24921596201741303),
            doubleArrayOf(5.0298021057780495, 1.501499571686426, 0.007153986369587084),
            doubleArrayOf(5.30257909269405, 1.3646186714272859, -0.07034298006141304),
            doubleArrayOf(5.58476409269405, 1.2988996714272858, -0.3407959800614129),
            doubleArrayOf(5.49461009299155, 1.445329571923056, -0.43201601656841304),
            doubleArrayOf(5.44857009269405, 1.473922671427286, -0.3773789800614129),
            doubleArrayOf(5.33528312241435, 1.5690300677323659, -0.40420603903641283),
            doubleArrayOf(5.11400709269405, 1.6807336714272858, -0.4238979800614129),
            doubleArrayOf(4.97576215063905, 1.896398684790186, -0.487735988224413),
            doubleArrayOf(4.72287204062305, 1.898232660105286, -0.24921596201741303),
        )

        val expectedResults = listOf(
            0.38216336, 0.290099, 0.25188425, 0.22851657, 0.25721811,
            0.26419225, 0.28602197, 0.31753234, 0.36413168, 0.38216336,
        )
        expectedResults.zip(deltas).forEach { (expectedResult, delta) ->
            assertEquals(expectedResult, getAzimuthalAngle(delta), 10e-8)
        }
    }

    @Test
    fun testGetArgMinAbsDiff() {
        val azimuthalAngle = listOf(
            0.38216336, 0.290099, 0.25188425, 0.22851657, 0.25721811,
            0.26419225, 0.28602197, 0.31753234, 0.36413168, 0.38216336,
        )
        val polarAngle = listOf(
            1.61971847, 1.56943344, 1.58364282, 1.63016257, 1.64668904,
            1.63755586, 1.6433518, 1.64938001, 1.66213683, 1.61971847,
        )

        val expectedAzimuthalIdx = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val expectedPolarIdx = intArrayOf(8, 7, 8, 8, 8, 8, 8, 8, 8, 8)
        val n = 16
        val polarIntervalLength = PI / n
        val azimuthalIntervalLength = 2*PI/n
        expectedAzimuthalIdx.withIndex().forEach {
            assertEquals(it.value, floor(azimuthalAngle[it.index]/azimuthalIntervalLength).toInt())
        }
        expectedPolarIdx.withIndex().forEach {
            assertEquals(it.value, floor(polarAngle[it.index]/polarIntervalLength).toInt())
        }
    }


    @Test
    fun testGetSphericalCoordinates() {
        val uv = getSphericalCoordinates(16)
        val expectedU = doubleArrayOf(
            0.19634954, 0.58904862, 0.9817477, 1.37444679, 1.76714587,
            2.15984495, 2.55254403, 2.94524311, 3.33794219, 3.73064128,
            4.12334036, 4.51603944, 4.90873852, 5.3014376, 5.69413668,
            6.08683577
        )
        val expectedV = doubleArrayOf(
            0.09817477, 0.29452431, 0.49087385, 0.68722339, 0.88357293,
            1.07992247, 1.27627202, 1.47262156, 1.6689711, 1.86532064,
            2.06167018, 2.25801972, 2.45436926, 2.6507188, 2.84706834,
            3.04341788
        )
        assertArrayAlmostEquals(expectedU, uv.u)
        assertArrayAlmostEquals(expectedV, uv.v)
    }

}