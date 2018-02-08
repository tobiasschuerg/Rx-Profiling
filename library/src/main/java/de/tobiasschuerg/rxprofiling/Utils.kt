package de.tobiasschuerg.rxprofiling

/**
 * Transforms milliseconds into dashes.
 * Each 100ms one dash is added.
 */
internal fun Long.dash(): String {
    val length = (this / 100).toInt()
    val outputBuffer = StringBuffer(length)
    for (i in 0 until length) {
        outputBuffer.append("#")
    }
    return outputBuffer.toString()
}