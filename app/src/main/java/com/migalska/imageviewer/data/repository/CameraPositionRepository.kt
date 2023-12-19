package com.migalska.imageviewer.data.repository

import com.migalska.imageviewer.data.entity.CameraPosition

interface CameraPositionRepository {

    suspend fun getById(id: Long): CameraPosition?

    suspend fun insertOrUpdate(cameraPosition: CameraPosition)
    suspend fun insertMany(positions: List<CameraPosition>)
}