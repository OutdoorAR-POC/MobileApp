package com.migalska.imageviewer.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect

data class StyledPath(
    val path: Path,
    val color: Color,
    val pathEffect: PathEffect? = null,
)
