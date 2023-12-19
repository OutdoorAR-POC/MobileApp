package com.migalska.imageviewer.data.repository.impl

import com.migalska.imageviewer.data.dao.CameraPositionDao
import com.migalska.imageviewer.data.entity.CameraPosition
import com.migalska.imageviewer.data.repository.CameraPositionRepository

class RoomCameraPositionRepository(private val dao: CameraPositionDao) : CameraPositionRepository {
    override suspend fun getById(id: Long): CameraPosition? {
        return dao.getById(id)
    }

    override suspend fun insertOrUpdate(cameraPosition: CameraPosition) {
        return dao.insert(cameraPosition)
    }

    override suspend fun insertMany(positions: List<CameraPosition>) {
        return dao.insertMany(positions)
    }
}