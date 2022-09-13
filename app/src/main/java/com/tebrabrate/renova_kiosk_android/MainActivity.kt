package com.tebrabrate.renova_kiosk_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class MainActivity : AppCompatActivity() {

    lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("ACA", "before connect")
        try {
            val options = IO.Options()
            options.forceNew = true
            options.reconnection = true
            options.reconnectionDelay = 2000
            options.reconnectionDelayMax = 5000
            socket = IO.socket("http://localhost:3001", options)
            socket.on(Socket.EVENT_CONNECT, onConnect)
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError)
            socket.connect()
        } catch (e: Exception) {
            // handler
            Log.d("ACA", "ne moze " + e.message)
        } finally {
            // optional finally block
            Log.d("ACA", "finally block of connect")
        }

        Log.d("ACA", "after connect")
    }

    var onConnect = Emitter.Listener {
        //After getting a Socket.EVENT_CONNECT which indicate socket has been connected to server,
        //send userName and roomName so that they can join the room.
        Log.d("ACA", "conectovao se")
    }

    var onDisconnect = Emitter.Listener {
        //After getting a Socket.EVENT_CONNECT which indicate socket has been connected to server,
        //send userName and roomName so that they can join the room.
        Log.d("ACA", "dissconectovao se" + it)
    }

    var onConnectionError = Emitter.Listener {
        //After getting a Socket.EVENT_CONNECT which indicate socket has been connected to server,
        //send userName and roomName so that they can join the room.
        Log.d("ACA", "error crko dabogda" + it[0])
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }
}