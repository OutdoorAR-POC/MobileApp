package com.migalska.imageviewer.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.migalska.imageviewer.viewmodel.StyledPath
import kotlin.math.roundToInt

class OverlayImagePainter constructor(
    private val image: ImageBitmap,
    private val paths: List<StyledPath> = emptyList(),
    private val srcOffset: IntOffset = IntOffset.Zero,
    private val srcSize: IntSize = IntSize(image.width, image.height),
) : Painter() {

    private val size: IntSize = validateSize(srcOffset, srcSize)
    override fun DrawScope.onDraw() {
        val dstSize = IntSize(
            this@onDraw.size.width.roundToInt(),
            this@onDraw.size.height.roundToInt()
        )
        drawImage(
            image,
            srcOffset,
            srcSize,
            dstSize = dstSize,
        )
        for (path in paths) {
            drawPath(
                color = path.color,
                path = path.path,
                style = Stroke(
                    width = 3.dp.toPx(),
                    pathEffect = path.pathEffect,
                ),
            )
        }
    }

    /**
     * Return the dimension of the underlying [ImageBitmap] as it's intrinsic width and height
     */
    override val intrinsicSize: Size get() = size.toSize()

    private fun validateSize(srcOffset: IntOffset, srcSize: IntSize): IntSize {
        require(
            srcOffset.x >= 0 &&
                    srcOffset.y >= 0 &&
                    srcSize.width >= 0 &&
                    srcSize.height >= 0 &&
                    srcSize.width <= image.width &&
                    srcSize.height <= image.height
        )
        return srcSize
    }
}