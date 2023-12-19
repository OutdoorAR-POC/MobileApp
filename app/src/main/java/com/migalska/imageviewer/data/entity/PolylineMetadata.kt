package com.migalska.imageviewer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "polyline_metadata")
data class PolylineMetadata(
    @PrimaryKey val id: Long,
    val path: String,
    val minX: Double = 0.0,
    val minY: Double = 0.0,
    val minZ: Double = 0.0,
    val maxX: Double = 0.0,
    val maxY: Double = 0.0,
    val maxZ: Double = 0.0,
)
