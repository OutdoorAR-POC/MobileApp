package com.migalska.imageviewer.viewmodel

import androidx.compose.ui.graphics.ImageBitmap

data class AnnotatedImage (
    val bitmap: ImageBitmap,
    val paths: List<StyledPath> = listOf(),
)