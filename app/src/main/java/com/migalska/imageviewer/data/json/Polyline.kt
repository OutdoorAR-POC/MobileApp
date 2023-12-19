package com.migalska.imageviewer.data.json

data class Polyline(
    val name: String,
    val vertices: List<Vertex>,
    val edges: List<Edge>,
) {

    val coordinates: Array<DoubleArray> = vertices.map {
            v -> doubleArrayOf(v.x, v.y, v.z)
    }.toTypedArray()

}

data class Point3D(
    val x: Double,
    val y: Double,
    val z: Double,
)

data class Vertex(
    val id: Int,
    val x: Double,
    val y: Double,
    val z: Double,
    val visibility_grid: DoubleArray = doubleArrayOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vertex

        if (id != other.id) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (!visibility_grid.contentEquals(other.visibility_grid)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + visibility_grid.contentHashCode()
        return result
    }
}


data class Edge(
    val vertex1: Int,
    val vertex2: Int,
    val red: Int,
    val green: Int,
    val blue: Int
)
