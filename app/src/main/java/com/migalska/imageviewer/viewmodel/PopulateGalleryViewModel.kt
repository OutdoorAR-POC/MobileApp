package com.migalska.imageviewer.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.migalska.imageviewer.data.entity.CameraIntrinsic
import com.migalska.imageviewer.data.entity.CameraPosition
import com.migalska.imageviewer.data.entity.Image
import com.migalska.imageviewer.data.repository.CameraIntrinsicRepository
import com.migalska.imageviewer.data.repository.CameraPositionRepository
import com.migalska.imageviewer.data.repository.ImageRepository
import com.migalska.imageviewer.sfm.Camera
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PopulateGalleryViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val intrinsicRepository: CameraIntrinsicRepository,
    private val positionRepository: CameraPositionRepository,
    private val application: Application,
) : ViewModel() {

    fun loadDatabase(name: String) {
        loadSfmData(name)
    }

    private fun loadSfmData(name: String) {
        val camera: Camera = parseSfm(name)
        val intrinsics: List<CameraIntrinsic> = parseCameraIntrinsics(camera)
        val positions: List<CameraPosition> = parseCameraPositions(camera)
        val images: List<Image> = parseImages(camera)
        viewModelScope.launch {
            intrinsicRepository.insertMany(intrinsics)
            positionRepository.insertMany(positions)
            imageRepository.insertMany(images)
        }
    }

    private fun parseSfm(name: String): Camera {
        application.assets.open(name).bufferedReader().use { reader ->
            val mapper = jacksonObjectMapper()
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            return mapper.readValue(reader)
        }
    }


    private fun parseCameraIntrinsics(camera: Camera): List<CameraIntrinsic> {
        val intrinsics: MutableList<CameraIntrinsic> = mutableListOf()
        for (intrinsic in camera.intrinsics) {
            intrinsics.add(
                CameraIntrinsic(
                    id = intrinsic.intrinsicId.toLong(),
                    pxFocalLength = intrinsic.pxFocalLength.toDouble(),
                    principalPoint = intrinsic.principalPoint.map { it.toDouble() }.toDoubleArray()
                )
            )
        }
        return intrinsics
    }

    private fun parseImages(camera: Camera): List<Image> {
        val images: MutableList<Image> = mutableListOf()
        for (view in camera.views) {
            val image = Image(
                id = view.viewId.toLong(),
                poseId = view.poseId.toLong(),
                intrinsicId = view.intrinsicId.toLong(),
                name = view.path.substring(view.path.lastIndexOf(File.separator) + 1).lowercase(),
                width = view.width.toInt(),
                height = view.height.toInt(),
            )
            images.add(image)
        }
        return images
    }

    private fun parseCameraPositions(camera: Camera): List<CameraPosition> {
        val positions: MutableList<CameraPosition> = mutableListOf()
        for (pose in camera.poses) {
            positions.add(
                CameraPosition(
                    id = pose.poseId.toLong(),
                    center = pose.pose.transform.center.map { it.toDouble() }.toDoubleArray(),
                    rotationMatrix = pose.pose.transform.rotation.map { it.toDouble() }
                        .toDoubleArray(),
                )
            )
        }
        return positions
    }

}
