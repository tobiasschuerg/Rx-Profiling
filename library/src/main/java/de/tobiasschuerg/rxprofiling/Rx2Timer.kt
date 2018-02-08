package de.stocard.dev

import io.reactivex.Flowable

/**
 * Measuring rx performance.
 *
 * Created by Tobias Schürg on 08.02.2018.
 */

fun <T> Flowable<T>.measureFirst(what: String, callback: (String) -> Unit): Flowable<T> {
    var timer: Long = 0
    return this
            .doOnSubscribe {
                callback("RX-TIMER: subscribed($what)")
                timer = System.currentTimeMillis()
            }
            .emitCounter { count ->
                if (count == 1) {
                    val millis = System.currentTimeMillis() - timer
                    callback("RX-TIMER: ${millis.dash()} $what emitted first item after $millis millis")
                } else {
                    callback("RX-TIMER: `$what` emitted $count. item")
                }
            }
}

fun <T> Flowable<T>.emitCounter(callback: (Int) -> Unit): Flowable<T> {
    var count: Int = 1
    return this.doOnNext { callback(count++) }
}


private fun Long.dash(): String {
    val length = (this / 100).toInt()
    val outputBuffer = StringBuffer(length)
    for (i in 0 until length) {
        outputBuffer.append("#")
    }
    return outputBuffer.toString()
}
