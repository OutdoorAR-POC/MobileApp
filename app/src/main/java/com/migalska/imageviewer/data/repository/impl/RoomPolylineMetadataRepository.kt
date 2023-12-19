package com.migalska.imageviewer.data.repository.impl

import com.migalska.imageviewer.data.dao.PolylineMetadataDao
import com.migalska.imageviewer.data.entity.PolylineMetadata
import com.migalska.imageviewer.data.repository.PolylineMetadataRepository

class RoomPolylineMetadataRepository(
    private val dao: PolylineMetadataDao
) : PolylineMetadataRepository {

    override suspend fun getPolylinesInNeighbourhood(
        x: Double,
        y: Double,
        distance: Double,
        n: Int,
    ): List<PolylineMetadata> {
        return listOf(
            PolylineMetadata(1, "visibility/n_$n/BluePolyline.json"),
            PolylineMetadata(2, "visibility/n_$n/RedPolyline.json"),
            PolylineMetadata(3, "visibility/n_$n/GreenPolyline.json"),
            PolylineMetadata(4, "visibility/n_$n/YellowPolyline.json"),
        )
    }

    override suspend fun insertMany(polylines: List<PolylineMetadata>) {
        return dao.insertMany(polylines)
    }

}