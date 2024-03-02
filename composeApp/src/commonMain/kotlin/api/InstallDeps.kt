package api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.serialization.json.Json

val Ktor = HttpClient(CIO) {
    // default validation to throw exceptions for non-2xx responses
    expectSuccess = true

    install(Logging)

    // use gson content negotiation for serialize or deserialize
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    install(HttpCache)
}

