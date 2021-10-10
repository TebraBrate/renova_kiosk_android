package com.tebrabrate.renova_kiosk_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tebrabrate.renova_kiosk_android.data.remote.ProductId
import com.tebrabrate.renova_kiosk_android.data.remote.RenovaRemoteService
import com.tebrabrate.renova_kiosk_android.data.remote.Subscribe
import com.tebrabrate.renova_kiosk_android.data.remote.Ticker
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val remoteDataService: RenovaRemoteService) :
    ViewModel() {

    private val BITCOIN_TICKER_SUBSCRIBE_MESSAGE = Subscribe(
        type = Subscribe.Type.SUBSCRIBE,
        productIds = listOf(ProductId.BTC_USD),
        channels = listOf(Subscribe.Channel.TICKER)
    )

    private val _ticker = MutableLiveData<Ticker>()

    val ticker: LiveData<Ticker>
        get() = _ticker

    suspend fun observeWebSocketEvent() {
        viewModelScope.launch {
            remoteDataService.observeWebSocketEvent()
                .filter { it is WebSocket.Event.OnConnectionOpened<*> }
                .collect {
                    remoteDataService.sendSubscribe(BITCOIN_TICKER_SUBSCRIBE_MESSAGE)
                }
        }
    }

    suspend fun observeTicker() {
        viewModelScope.launch {
            remoteDataService.observeTicker()
                .collect {
                    _ticker.value = it
                }
        }
    }

}
