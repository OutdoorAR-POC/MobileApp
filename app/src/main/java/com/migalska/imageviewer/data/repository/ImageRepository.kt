package com.migalska.imageviewer.data.repository

import com.migalska.imageviewer.data.entity.Image

interface ImageRepository {

    suspend fun insertOrUpdate(image: Image)

    suspend fun insertMany(images: List<Image>)

    suspend fun getByName(name: String): Image?

}
