package com.migalska.imageviewer.data

import com.migalska.imageviewer.assertArrayAlmostEquals
import com.migalska.imageviewer.data.entity.CameraIntrinsic
import org.junit.Test


internal class CameraIntrinsicTest {

    @Test
    fun testGetIntrinsicMatrix() {
        val expectedIntrinsicMatrix = listOf(
            doubleArrayOf(3.0018525e+03, 0.00000000e+00, 2.0005393e+03, 0.00000000e+00),
            doubleArrayOf(0.00000000e+00, 3.0018525e+03, 1.4954928e+03, 0.00000000e+00),
            doubleArrayOf(0.00000000e+00, 0.00000000e+00, 1.00000000e+00, 0.00000000e+00)
        )
        val cameraIntrinsic = CameraIntrinsic(
            id=2578075835,
            pxFocalLength=3001.8525,
            principalPoint= doubleArrayOf(2000.5393, 1495.4928),
        )
        val result = cameraIntrinsic.getIntrinsicMatrix()
        for ((actualRow, expectedRow) in result.data.zip(expectedIntrinsicMatrix)) {
            assertArrayAlmostEquals(expectedRow, actualRow)
        }
    }
}