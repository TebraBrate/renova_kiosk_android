package com.tebrabrate.renova_kiosk_android

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.net.URISyntaxException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var mSocket: Socket;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val opts = IO.Options();
            opts.transports = arrayOf(WebSocket.NAME)
            mSocket = IO.socket("http://localhost:3001", opts)
            mSocket.connect()
            mSocket.on("events", onNewEvent)
            mSocket.on(Socket.EVENT_CONNECT, onConnect)
            mSocket.on(Socket.EVENT_DISCONNECT, onDisConnect)
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError)
        } catch (e: URISyntaxException) {
            //TODO see how to handle error
        }

        lifecycleScope.launch {

        }

        viewModel.events.observe(this) { ticker ->
            Timber.d("MARKO ticker is $ticker")
        }
    }

    private val onNewEvent = Emitter.Listener { event ->
        runOnUiThread(Runnable {
            Timber.d("DATA ${event[0]}")
        })
    }

    private val onConnect = Emitter.Listener { event ->
        runOnUiThread(Runnable {
            Timber.d("CONNECTED $event")
            mSocket.emit("events")
        })
    }

    private val onDisConnect = Emitter.Listener { event ->
        runOnUiThread(Runnable {
           // val data = event[0] as JSONObject
            Timber.d("DISCONNECTED ${event[0]}")
        })
    }

    private val onConnectionError = Emitter.Listener { event ->
        runOnUiThread(Runnable {
//            val data = event[0] as JSONObject
            Timber.d("ERROR ${event[0]}")
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
    }
}
