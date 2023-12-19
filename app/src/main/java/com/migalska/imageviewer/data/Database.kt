package com.migalska.imageviewer.data


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.migalska.imageviewer.Converters
import com.migalska.imageviewer.data.dao.CameraIntrinsicDao
import com.migalska.imageviewer.data.dao.CameraPositionDao
import com.migalska.imageviewer.data.dao.ImageDao
import com.migalska.imageviewer.data.dao.PolylineMetadataDao
import com.migalska.imageviewer.data.entity.CameraIntrinsic
import com.migalska.imageviewer.data.entity.CameraPosition
import com.migalska.imageviewer.data.entity.Image
import com.migalska.imageviewer.data.entity.PolylineMetadata

@Database(
    entities = [
        Image::class,
        CameraIntrinsic::class,
        CameraPosition::class,
        PolylineMetadata::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CameraPositionsDatabase : RoomDatabase() {

    abstract val cameraPositionDao: CameraPositionDao
    abstract val cameraIntrinsicDao: CameraIntrinsicDao
    abstract val imageDao: ImageDao
    abstract val polylineMetadataDao: PolylineMetadataDao
}
