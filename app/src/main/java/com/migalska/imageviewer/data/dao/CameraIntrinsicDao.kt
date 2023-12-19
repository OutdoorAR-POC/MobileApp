package com.migalska.imageviewer.data.dao

import androidx.room.*
import com.migalska.imageviewer.data.entity.CameraIntrinsic

@Dao
abstract class CameraIntrinsicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(cameraIntrinsic: CameraIntrinsic)

    @Query("SELECT * FROM camera_intrinsic WHERE id = :id")
    abstract suspend fun getById(id: Long): CameraIntrinsic?

    @Transaction
    open suspend fun insertMany(intrinsics: List<CameraIntrinsic>){
        for (intrinsic in intrinsics) insert(intrinsic)
    }
}
