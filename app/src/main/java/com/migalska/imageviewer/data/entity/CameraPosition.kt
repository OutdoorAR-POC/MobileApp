package com.migalska.imageviewer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camera_position")
data class CameraPosition(
    @PrimaryKey val id: Long,
    val rotationMatrix: DoubleArray,
    val center: DoubleArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CameraPosition

        if (id != other.id) return false
        if (!rotationMatrix.contentEquals(other.rotationMatrix)) return false
        if (!center.contentEquals(other.center)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + rotationMatrix.contentHashCode()
        result = 31 * result + center.contentHashCode()
        return result
    }

}
