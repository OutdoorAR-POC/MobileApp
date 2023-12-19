package com.migalska.imageviewer.data.dao

import androidx.room.*
import com.migalska.imageviewer.data.entity.Image

@Dao
abstract class ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(image: Image)

    @Query("SELECT * FROM image WHERE name = :name")
    abstract suspend fun getByName(name: String): Image?

    @Transaction
    open suspend fun insertMultiple(images: List<Image>) {
        for (image in images) insert(image)
    }
}