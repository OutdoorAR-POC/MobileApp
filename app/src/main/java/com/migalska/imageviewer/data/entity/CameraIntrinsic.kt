package com.migalska.imageviewer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix

@Entity(tableName = "camera_intrinsic")
data class CameraIntrinsic(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val pxFocalLength: Double,
    val principalPoint: DoubleArray = doubleArrayOf(),
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CameraIntrinsic

        if (id != other.id) return false
        if (pxFocalLength != other.pxFocalLength) return false
        if (!principalPoint.contentEquals(other.principalPoint)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + pxFocalLength.hashCode()
        result = 31 * result + principalPoint.contentHashCode()
        return result
    }

    fun getIntrinsicMatrix(): RealMatrix {
        return MatrixUtils.createRealMatrix(
            arrayOf(
                doubleArrayOf(
                    this.pxFocalLength,
                    0.0,
                    this.principalPoint[0],
                    0.0
                ),
                doubleArrayOf(
                    0.0,
                    this.pxFocalLength,
                    this.principalPoint[1],
                    0.0
                ),
                doubleArrayOf(0.0, 0.0, 1.0, 0.0),
            )
        )

    }

}
