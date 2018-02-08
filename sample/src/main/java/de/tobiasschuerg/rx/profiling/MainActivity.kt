package de.tobiasschuerg.rx.profiling

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import de.stocard.dev.measureFirst
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    val cd = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        val d = Flowable.interval(1, TimeUnit.SECONDS)
                .delay(100, TimeUnit.MILLISECONDS)
                .measureFirst("test", { Log.i("MainActivity", it) })
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    text.text = "Item $it"
                }, {
                    Log.i("MainActivity", "Error $it")
                })
        cd.add(d)
    }

    override fun onPause() {
        super.onPause()
        cd.clear()
    }
}
