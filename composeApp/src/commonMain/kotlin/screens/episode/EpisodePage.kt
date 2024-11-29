package screens.episode

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import api.impl.ProxyApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.serialization.json.Json
import model.EpisodeTile
import model.PlayerData
import okio.internal.commonToUtf8String
import video.Video
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private val linksCache = HashMap<String, PlayerData>()


class EpisodePage(private val tileData: EpisodeTile) : Screen {

    @Composable
    override fun Content() {
        val url = listOf(ProxyApi.getCachedEndpoint(), tileData.animeSlug, tileData.episodeSlug)
            .joinToString(separator = "/")

        val navigator = LocalNavigator.current
        var playerData : PlayerData? by remember { mutableStateOf(null) }

        Scaffold(
            topBar = {
                IconButton(onClick = {
                    navigator?.pop()
                }) {
                    Icon(painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),contentDescription = "back")
                }
            }
        ) {
            if (playerData == null) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                    WebLoader(url) {
                        playerData = it
                    }
                }
            } else {
                VideoPlayer(playerData!!, tileData.episodeSlug)
            }
        }
    }

    @Composable
    private fun WebLoader(url: String, onRetrievePlayerData: (PlayerData) -> Unit) {
        if (linksCache.containsKey(url)) {
            linksCache[url]?.let { onRetrievePlayerData(it) }
        } else {
            val state = rememberWebViewState(url,
                mapOf("Access-Control-Allow-Origin" to "*")
            )
            val jsBridge = rememberWebViewJsBridge()

            LaunchedEffect(jsBridge) {
                jsBridge.register(
                    VideoFetcherJsMessageHandler {
                        linksCache[url] = it
                        onRetrievePlayerData(it)
                    }
                )
            }

            WebView(
                state = state,
                webViewJsBridge = jsBridge
            )
        }
    }

    @Composable
    fun VideoPlayer(playerData: PlayerData, episodeSlug: String?) {
        Text("LOADED")
        playerData.file?.let {
            Video(
                modifier = Modifier.fillMaxSize(),
                url = "https:${it}",
                slug = episodeSlug ?: "null"
            )
        }
    }
}

class VideoFetcherJsMessageHandler(private val onRetrievePlayerData: (PlayerData) -> Unit) : IJsMessageHandler {
    override fun methodName(): String {
        return "Hack"
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        val decode = Base64.decode(message.params)
        val jsonInstance = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
        val playerData = jsonInstance.decodeFromString<PlayerData>(decode.commonToUtf8String())
        onRetrievePlayerData(playerData)
    }
}