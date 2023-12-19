package com.migalska.imageviewer.data.dao

import androidx.room.*
import com.migalska.imageviewer.data.entity.PolylineMetadata

@Dao
abstract class PolylineMetadataDao {

    @Query("SELECT * FROM polyline_metadata WHERE id = :id")
    abstract suspend fun getById(id: Long): PolylineMetadata?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(polyline: PolylineMetadata)

    @Transaction
    open suspend fun insertMany(polylines: List<PolylineMetadata>) {
        for (polyline in polylines) insert(polyline)
    }

}
