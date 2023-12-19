package com.migalska.imageviewer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch


@Composable
fun BoxWithLayout(content: @Composable BoxScope.()->Unit) {
    Box {
        content()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScaffold(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                onNavigationIconClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerHeader()
            DrawerBody(
                menuItems = listOf(
                    MenuItem(
                        MainActivity.VIEW_ID,
                        title = stringResource(id = R.string.title_activity_main),
                        contentDescription = "Go to home screen",
                        icon = Icons.Default.Home,
                    ),
                    MenuItem(
                        PathViewerActivity.VIEW_ID,
                        title = stringResource(id = R.string.title_path_viewer_activity),
                        contentDescription = "Go to path viewer screen",
                        icon = Icons.Default.Star,
                    ),
                ),
                onItemClick = {
                    when (it.id) {
                        MainActivity.VIEW_ID -> MainActivity.start(context)
                        PathViewerActivity.VIEW_ID -> PathViewerActivity.start(context)
                        else -> println("Unrecognized activity")
                    }
                }
            )
        }
    ) {
        content()
    }
}
