package com.migalska.imageviewer.data.dao

import androidx.room.*
import com.migalska.imageviewer.data.entity.CameraPosition

@Dao
abstract class CameraPositionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(cameraPosition: CameraPosition)

    @Query("SELECT * FROM camera_position WHERE id = :id")
    abstract suspend fun getById(id: Long): CameraPosition?

    @Transaction
    open suspend fun insertMany(positions: List<CameraPosition>) {
        for (position in positions) insert(position)
    }
}
