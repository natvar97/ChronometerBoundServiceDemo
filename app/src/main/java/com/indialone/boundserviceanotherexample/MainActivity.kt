package com.indialone.boundserviceanotherexample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var btnStartService: Button
    private lateinit var btnStopService: Button
    private lateinit var tvTime: TextView

    private lateinit var mBoundService: BoundService
    private var mServiceBound = false

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service != null) {
                mBoundService = (service as BoundService.MyBinder).getService()
                mServiceBound = true
                Log.e("MainActivity", "Service Connected")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mServiceBound = false
            Log.e("Main Activity", "Service Disconnected")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartService = findViewById(R.id.print_timestamp)
        btnStopService = findViewById(R.id.stop_service)
        tvTime = findViewById(R.id.timestamp_text)

        btnStopService.setOnClickListener {
            if (mServiceBound) {
                unbindService(serviceConnection)
                mServiceBound = false
            }
            stopService(commonIntent())
        }

        btnStartService.setOnClickListener {
            if (mServiceBound) {
                tvTime.text = mBoundService.getTimestamp()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        startService()
    }

    override fun onStop() {
        super.onStop()

        if (mServiceBound) {
            unbindService(serviceConnection)
            mServiceBound = false
        }
    }

    private fun commonIntent(): Intent {
        return Intent(this, BoundService::class.java)
    }

    private fun startService() {
        startService(commonIntent())
        bindService()
    }

    private fun bindService() {
        bindService(commonIntent(), serviceConnection, Context.BIND_AUTO_CREATE)
    }

}