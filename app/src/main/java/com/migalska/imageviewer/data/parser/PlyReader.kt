package com.migalska.imageviewer.data.parser

import com.migalska.imageviewer.data.json.Edge
import com.migalska.imageviewer.data.json.Polyline
import com.migalska.imageviewer.data.json.Vertex

private data class Feature(val name: String, val type: String)

fun parsePolyline(name: String, lines: List<String>): Polyline {
    val elements = mutableListOf<String>()
    val features = HashMap<String, List<Feature>>()
    val howMany = HashMap<String, Int>()
    var featureMap: MutableList<Feature>? = null
    var currentElement: String? = null
    var index = 0
    var line = lines[index]
    while (!line.startsWith("ply")) {
        index += 1
        line = lines[index]
    }
    while (!line.startsWith("format")) {
        index += 1
        line = lines[index]
    }
    while (line.isNotBlank()) {
        if (line.startsWith("element")) {
            if (featureMap != null && currentElement != null) {
                // previous element
                features[currentElement] = featureMap
            }
            print(line)
            val tokens = line.split("\\s+".toRegex())
            currentElement = tokens[1]
            elements.add(currentElement)
            howMany[currentElement] = tokens[2].toInt()
            featureMap = mutableListOf() // new element's features
        } else if (line.startsWith("property")) {
            val tokens = line.split("\\s+".toRegex())
            featureMap?.add(Feature(name = tokens[2], type = tokens[1]))  // name: type
        } else if (line.startsWith("end_header")) {
            if (featureMap != null && currentElement != null) {
                // previous element
                features[currentElement] = featureMap
            }
            break // header complete
        }
        index += 1
        line = lines[index]
    }

    val vertices = mutableListOf<Vertex>()
    val edges = mutableListOf<Edge>()
    val parsedFeatures = mutableListOf<String>()

    var vertexIndex = 0

    for (element in elements) {
        val expectedFeatures = features[element]

        for (i in 0 until howMany[element]!!) {
            parsedFeatures.clear()

            while (parsedFeatures.size < expectedFeatures!!.size) {
                index += 1
                line = lines[index]
                val tokens = line.split("\\s+".toRegex())
                parsedFeatures.addAll(tokens)
            }
            if (element == "vertex") {
                vertices.add(
                    Vertex(
                        vertexIndex,
                        parsedFeatures[0].toDouble(),
                        parsedFeatures[1].toDouble(),
                        parsedFeatures[2].toDouble(),
                    )
                )
                vertexIndex += 1

            } else if (element == "edge") {
                edges.add(
                    Edge(
                        parsedFeatures[0].toInt(),
                        parsedFeatures[1].toInt(),
                        parsedFeatures[2].toInt(),
                        parsedFeatures[3].toInt(),
                        parsedFeatures[4].toInt(),
                    )
                )
            }
        }
    }
    return Polyline(name, vertices, edges)

}
