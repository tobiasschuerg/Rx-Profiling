package de.tobiasschuerg.rxprofiling

import io.reactivex.Flowable

/**
 * Measuring rx performance.
 *
 * Created by Tobias Schürg on 08.02.2018.
 */


/**
 * Logs when the subscription is set up,
 * measures how log it takes to emit the first item
 * and triggers on each element update.
 */
fun <T> Flowable<T>.measureFirst(what: String, loggingFunction: (String) -> Unit): Flowable<T> {
    var timer: Long = 0
    return this
            .doOnSubscribe {
                loggingFunction("RX-TIMER: subscribed($what)")
                timer = System.currentTimeMillis()
            }
            .emitCounter { count ->
                if (count == 1) {
                    val millis = System.currentTimeMillis() - timer
                    loggingFunction("RX-TIMER: ${millis.dash()} $what emitted first item after $millis millis")
                } else {
                    loggingFunction("RX-TIMER: `$what` emitted $count times")
                }
            }
}

/**
 * Measures the time until the first item is emitted.
 */
fun <T> Flowable<T>.measureFirst(callback: (Long) -> Unit): Flowable<T> {
    var timer: Long = 0
    return this
            .doOnSubscribe { timer = System.currentTimeMillis() }
            .emitCounter { count ->
                if (count == 1) {
                    val millis = System.currentTimeMillis() - timer
                    callback(millis)
                }
            }
}

/**
 * Counts how often this steam emits an item.
 */
fun <T> Flowable<T>.emitCounter(callback: (Int) -> Unit): Flowable<T> {
    var count: Int = 1
    return this.doOnNext { callback(count++) }
}


