package com.migalska.imageviewer.util

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix


const val COLUMN_ORDER = "F"
const val ROW_ORDER = "R"

fun RealMatrix.reshape(cols: Int, rows: Int, order: String = ROW_ORDER): RealMatrix {
    val oldSize = this.rowDimension * this.columnDimension
    val newSize = cols * rows
    assert(newSize == oldSize)

    val output = MatrixUtils.createRealMatrix(rows, cols)

    if (order == ROW_ORDER) {
        for (i in 0 until oldSize) {
            val newRow = i.div(cols)
            val newCol = i % cols
            val oldRow = i.div(this.columnDimension)
            val oldCol = i % this.columnDimension
            output.setEntry(newRow, newCol, this.data[oldRow][oldCol])
        }
    } else if (order == COLUMN_ORDER) {
        for (i in 0 until oldSize) {
            val newCol = i.div(rows)
            val newRow = i % rows
            val oldCol = i.div(this.rowDimension)
            val oldRow = i % this.rowDimension
            output.setEntry(newRow, newCol, this.data[oldRow][oldCol])
        }
    } else {
        throw IllegalArgumentException("Unknown order: $order")
    }

    return output
}