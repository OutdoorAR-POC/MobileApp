package com.migalska.imageviewer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
data class Image(
    @PrimaryKey val id: Long,
    val poseId: Long,
    val intrinsicId: Long,
    val name: String,
    val width: Int,
    val height: Int
)
