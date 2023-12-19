package com.migalska.imageviewer.data.repository.impl

import com.migalska.imageviewer.data.dao.ImageDao
import com.migalska.imageviewer.data.entity.Image
import com.migalska.imageviewer.data.repository.ImageRepository

class RoomImageRepository(private val dao: ImageDao) : ImageRepository {
    override suspend fun insertOrUpdate(image: Image) {
        dao.insert(image)
    }

    override suspend fun insertMany(images: List<Image>) {
        for (image in images) dao.insert(image)
    }

    override suspend fun getByName(name: String): Image? {
        return dao.getByName(name)
    }

}