package de.tobiasschuerg.rx.profiling

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import de.tobiasschuerg.rxprofiling.emitCounter
import de.tobiasschuerg.rxprofiling.measureFirst
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    private val cd = CompositeDisposable()
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        val d = Flowable.interval(1000, TimeUnit.MILLISECONDS)
                .emitCounter { Log.d(TAG, "count $it") }
                .filter { it.toInt() % 3 == 0 }
                .measureFirst("test", { Log.i(TAG, it) })
                .measureFirst { Log.d(TAG, "It took $it millis to emit the first item") }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    text.text = "Item $it"
                }, {
                    Log.i(TAG, "Error $it")
                })
        cd.add(d)
    }

    override fun onPause() {
        super.onPause()
        cd.clear()
    }
}
