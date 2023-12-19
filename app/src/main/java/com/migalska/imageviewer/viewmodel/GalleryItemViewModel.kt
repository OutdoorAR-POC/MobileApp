package com.migalska.imageviewer.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.migalska.imageviewer.use_case.AnnotateImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GalleryItemViewModel @Inject constructor(
    private val annotateImageUseCase: AnnotateImageUseCase,
) : ViewModel() {

    var annotatedImage by mutableStateOf<AnnotatedImage?>(null)
        private set

    var selectedItem by mutableStateOf<String?>(null)
        private set

    fun selectItem(imageFileName: String) {
        selectedItem = imageFileName

        viewModelScope.launch {
            annotatedImage = annotateImageUseCase(imageFileName)
        }
    }
}