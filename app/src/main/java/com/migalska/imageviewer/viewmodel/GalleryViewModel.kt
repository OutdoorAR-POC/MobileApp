package com.migalska.imageviewer.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val imagesPaths: Array<String>? = application.assets.list("capturedImages")

    var currentImage by mutableStateOf<String?>(null)
        private set

    private var currentImageIndex by mutableStateOf<Int>(0)

    init {
        imagesPaths?.let{
            currentImage = it[currentImageIndex]
            savedStateHandle["imageFileName"] = currentImage
        }
    }

    fun onEvent(event: GalleryEvent) {
        imagesPaths?.let {
            currentImageIndex = when (event) {
                is GalleryEvent.OnPreviousClick -> {
                    if (currentImageIndex > 0) currentImageIndex - 1 else it.size - 1
                }
                is GalleryEvent.OnNextClick -> {
                    if (currentImageIndex < it.size - 1) currentImageIndex + 1 else 0
                }
            }
            currentImage = it[currentImageIndex]
            savedStateHandle["imageFileName"] = currentImage
        }
    }
}