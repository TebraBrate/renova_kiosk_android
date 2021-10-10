package com.tebrabrate.renova_kiosk_android.data.remote

import com.google.gson.annotations.SerializedName
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow

interface RenovaRemoteService {

    @Receive
    fun observeWebSocketEvent(): Flow<WebSocket.Event>

    @Send
    fun sendSubscribe(subscribe: Subscribe)

    @Receive
    fun observeTicker(): Flow<Ticker>
}

data class Ticker(
    val type: String,
    @SerializedName("product_id")
    val product: ProductId,
    val sequence: Long,
    val time: String,
    val price: String,
    val side: String,
    @SerializedName("last_size")
    val size: String,
    @SerializedName("best_bid")
    val bestBid: String,
    @SerializedName("best_ask")
    val bestAsk: String
)

data class Subscribe(
    val type: Type,
    @SerializedName("product_ids")
    val productIds: List<ProductId>,
    val channels: List<Channel>
) {
    enum class Type(val text: String) {
        SUBSCRIBE("subscribe"),
        UNSUBSCRIBE("unsubscribe")
    }

    enum class Channel(val text: String) {
        TICKER("ticker"),
        LEVEL2("level2"),
        HEARTBEAT("heartbeat")
    }
}

enum class ProductId(val text: String) {
    BTC_USD("BTC-USD"),
    ETH_USD("ETH-USD"),
    LTC_USD("LTC-USD")
}
