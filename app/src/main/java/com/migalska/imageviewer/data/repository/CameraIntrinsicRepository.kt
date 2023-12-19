package com.migalska.imageviewer.data.repository

import com.migalska.imageviewer.data.entity.CameraIntrinsic

interface CameraIntrinsicRepository {

    suspend fun getById(id: Long): CameraIntrinsic?

    suspend fun insertOrUpdate(cameraIntrinsic: CameraIntrinsic)

    suspend fun insertMany(intrinsics: List<CameraIntrinsic>)
}