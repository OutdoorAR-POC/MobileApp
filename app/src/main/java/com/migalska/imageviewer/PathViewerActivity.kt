package com.migalska.imageviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.migalska.imageviewer.ui.theme.ImageViewerTheme
import com.migalska.imageviewer.util.OverlayImagePainter
import com.migalska.imageviewer.viewmodel.GalleryEvent
import com.migalska.imageviewer.viewmodel.GalleryItemViewModel
import com.migalska.imageviewer.viewmodel.GalleryViewModel
import com.migalska.imageviewer.viewmodel.PopulateGalleryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PathViewerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageViewerTheme {
                GalleryLoader()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = MaterialTheme.colors.background,
                ) {
                    Gallery()
                }
            }
        }
    }

    companion object {
        const val VIEW_ID = "PathViewer"

        fun start(context: Context) {
            val intent = Intent(context, PathViewerActivity::class.java)
            context.startActivity(intent)
        }
    }

}

@Composable
fun GalleryLoader(
    viewModel: PopulateGalleryViewModel = hiltViewModel()
) {
    viewModel.loadDatabase("paths/cameras.sfm")
}

@Composable
fun Gallery(
    viewModel: GalleryViewModel = hiltViewModel()
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                  viewModel.onEvent(GalleryEvent.OnPreviousClick)
            },
            shape = CircleShape,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Previous",
                modifier = Modifier.size(20.dp)
            )
        }
        viewModel.currentImage?.let { GalleryItem(it) }
        Button(
            onClick = {
                viewModel.onEvent(GalleryEvent.OnNextClick)
            },
            shape = CircleShape,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun GalleryItem(
    imagePath: String,
    viewModel: GalleryItemViewModel = hiltViewModel()
) {
    if (viewModel.selectedItem != imagePath) {
        viewModel.selectItem(imagePath)
    }

    viewModel.annotatedImage?.let {
        Image(
            painter = OverlayImagePainter(it.bitmap, it.paths),
            contentDescription = "Image",
            modifier = Modifier.padding(16.dp),
        )
    }
}
