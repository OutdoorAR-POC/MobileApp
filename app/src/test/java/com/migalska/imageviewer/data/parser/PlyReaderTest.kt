package com.migalska.imageviewer.data.parser

import org.junit.Assert.assertEquals
import org.junit.Test

internal class PlyReaderTest {

    @Test
    fun testParsePolyline() {
        val samplePolyline = listOf(
            "ply",
            "format ascii 1.0",
            "element vertex 6",
            "property float x",
            "property float y",
            "property float z",
            "property uchar red",
            "property uchar green",
            "property uchar blue",
            "element edge 5",
            "property int vertex1",
            "property int vertex2",
            "property uchar red",
            "property uchar green",
            "property uchar blue",
            "end_header",
            "2.097919940948E+00 1.610359922051E-02 2.190779924393E+00",
            " 255 255 255",
            "1.971269965172E+00 2.922860085964E-01 2.525049924850E+00",
            " 255 255 255",
            "1.777729988098E+00 6.717010140419E-01 2.703690052032E+00",
            " 255 255 255",
            "1.789610028267E+00 1.110649943352E+00 2.692329883575E+00",
            " 255 255 255",
            "1.545259952545E+00 1.343610048294E+00 2.792229890823E+00",
            " 255 255 255",
            "1.419679999352E+00 2.067719936371E+00 2.833199977875E+00",
            " 255 255 255",
            "0 1 0 255 127",
            "1 2 0 255 127",
            "2 3 0 255 127",
            "3 4 0 255 127",
            "4 5 0 255 127",
        )
        val result = parsePolyline("sample", samplePolyline)
        assertEquals("sample", result.name)
        assertEquals(6, result.vertices.size)
        assertEquals(5, result.edges.size)
    }

}