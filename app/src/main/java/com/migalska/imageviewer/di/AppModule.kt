package com.migalska.imageviewer.di

import android.app.Application
import androidx.room.Room
import com.migalska.imageviewer.data.*
import com.migalska.imageviewer.data.repository.CameraIntrinsicRepository
import com.migalska.imageviewer.data.repository.CameraPositionRepository
import com.migalska.imageviewer.data.repository.ImageRepository
import com.migalska.imageviewer.data.repository.PolylineMetadataRepository
import com.migalska.imageviewer.data.repository.impl.RoomCameraIntrinsicRepository
import com.migalska.imageviewer.data.repository.impl.RoomCameraPositionRepository
import com.migalska.imageviewer.data.repository.impl.RoomImageRepository
import com.migalska.imageviewer.data.repository.impl.RoomPolylineMetadataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): CameraPositionsDatabase {
        return Room.databaseBuilder(
            app,
            CameraPositionsDatabase::class.java,
            name = "sobotka_20230320_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideImageRepository(db: CameraPositionsDatabase): ImageRepository {
        return RoomImageRepository(db.imageDao)
    }

    @Provides
    @Singleton
    fun provideCameraPositionRepository(db: CameraPositionsDatabase): CameraPositionRepository {
        return RoomCameraPositionRepository(db.cameraPositionDao)
    }

    @Provides
    @Singleton
    fun provideCameraIntrinsicRepository(db: CameraPositionsDatabase): CameraIntrinsicRepository {
        return RoomCameraIntrinsicRepository(db.cameraIntrinsicDao)
    }

    @Provides
    @Singleton
    fun providePolylineMetadataRepository(db: CameraPositionsDatabase): PolylineMetadataRepository {
        return RoomPolylineMetadataRepository(db.polylineMetadataDao)
    }

}