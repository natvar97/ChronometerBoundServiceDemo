package com.indialone.boundserviceanotherexample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import android.widget.Chronometer

class BoundService : Service() {

    inner class MyBinder : Binder() {
        fun getService(): BoundService {
            return this@BoundService
        }
    }

    private lateinit var mChronometer: Chronometer
    private var mBinder: MyBinder = MyBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()

        mChronometer = Chronometer(this)
        mChronometer.base = SystemClock.currentThreadTimeMillis()
        mChronometer.start()

    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mChronometer.stop()
    }

    fun getTimestamp(): String? {
        val elapsedMillis = (SystemClock.elapsedRealtime()
                - mChronometer.base)
        val hours = (elapsedMillis / 3600000).toInt()
        val minutes = (elapsedMillis - hours * 3600000).toInt() / 60000
        val seconds = (elapsedMillis - hours * 3600000 - minutes * 60000).toInt() / 1000
        val millis = (elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000).toInt()
        return "$hours:$minutes:$seconds:$millis"
    }

}