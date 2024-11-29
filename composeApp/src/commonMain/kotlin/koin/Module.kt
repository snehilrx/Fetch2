package koin

import api.IKickassAnimeApi
import api.IProxyApi
import api.impl.KickassAnimeApi
import api.impl.ProxyApi
import com.otaku.fetch2.Database
import database.DriverFactory
import database.createDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(CIO) {
            // default validation to throw exceptions for non-2xx responses
            expectSuccess = true
            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP call --- $message", )
                    }
                }
            }
            followRedirects = true

            // use gson content negotiation for serialize or deserialize
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(HttpCache)
        }
    }
}



val dbModule = module {
    single {
        createDatabase(DriverFactory())
    }
}

val apiModule = module {
    single<IProxyApi> {
        ProxyApi(get(), get())
    }
    single <IKickassAnimeApi> {
        KickassAnimeApi(get(), get())
    }
}