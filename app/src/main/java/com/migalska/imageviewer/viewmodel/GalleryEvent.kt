package com.migalska.imageviewer.viewmodel

sealed class GalleryEvent {
    object OnNextClick: GalleryEvent()
    object OnPreviousClick: GalleryEvent()
}
