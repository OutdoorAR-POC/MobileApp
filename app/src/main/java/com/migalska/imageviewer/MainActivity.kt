package com.migalska.imageviewer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.migalska.imageviewer.ui.theme.ImageViewerTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageViewerTheme {
                MainScaffold {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text =
                            """
                    This is a POC application for the conference paper:
                    A. Migalska, "Real-time 3D Information Visualization on Mobile Devices: Efficient Occlusion Detection for Geospatial Applications", GRAPP 19 (2024).
                     
                     Open Navigation drawer to see available options.
                """.trimIndent(),
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val VIEW_ID = "Home"

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
