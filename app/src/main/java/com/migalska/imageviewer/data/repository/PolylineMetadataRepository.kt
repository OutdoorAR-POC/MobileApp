package com.migalska.imageviewer.data.repository

import com.migalska.imageviewer.data.entity.PolylineMetadata

interface PolylineMetadataRepository {

    suspend fun getPolylinesInNeighbourhood(
        x: Double, y: Double, distance: Double, n: Int
    ): List<PolylineMetadata>

    suspend fun insertMany(polylines: List<PolylineMetadata>)
}