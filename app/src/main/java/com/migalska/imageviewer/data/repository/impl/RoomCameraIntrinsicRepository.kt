package com.migalska.imageviewer.data.repository.impl

import com.migalska.imageviewer.data.dao.CameraIntrinsicDao
import com.migalska.imageviewer.data.entity.CameraIntrinsic
import com.migalska.imageviewer.data.repository.CameraIntrinsicRepository

class RoomCameraIntrinsicRepository(
    private val dao: CameraIntrinsicDao
) : CameraIntrinsicRepository {

    override suspend fun getById(id: Long): CameraIntrinsic? {
        return dao.getById(id)
    }

    override suspend fun insertOrUpdate(cameraIntrinsic: CameraIntrinsic) {
        return dao.insert(cameraIntrinsic)
    }

    override suspend fun insertMany(intrinsics: List<CameraIntrinsic>) {
        return dao.insertMany(intrinsics)
    }

}