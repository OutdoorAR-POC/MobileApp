package com.migalska.imageviewer

import org.junit.Assert
import kotlin.math.abs


fun assertArrayAlmostEquals(
    expected: DoubleArray,
    actual: DoubleArray,
    epsilon: Double = 0.00000001,
) {
    Assert.assertEquals(
        "Expected <${expected.size}>, actual <${actual.size}>, " +
                "should not differ in size",
        expected.size,
        actual.size,
    )
    for (i in actual.indices) {
        Assert.assertTrue(
            "Index <$i>, expected <${expected[i]}>, actual <${actual[i]}>, " +
                    "should differ no more than <$epsilon>.",
            abs(expected[i] - actual[i]) <= epsilon
        )
    }
}
