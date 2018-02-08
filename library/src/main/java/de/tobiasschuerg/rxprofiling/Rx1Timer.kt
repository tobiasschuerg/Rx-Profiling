package de.tobiasschuerg.rxprofiling

import rx.Observable

/**
 * Measuring rx performance.
 *
 * Created by Tobias Schürg on 08.02.2018.
 */

fun <T> Observable<T>.measureFirst(what: String, callback: (String) -> Unit): Observable<T> {
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

fun <T> Observable<T>.emitCounter(callback: (Int) -> Unit): Observable<T> {
    var count: Int = 1
    return this.doOnNext { callback(count++) }
}


