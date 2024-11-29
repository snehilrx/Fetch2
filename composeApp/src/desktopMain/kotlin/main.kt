import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.datlag.kcef.KCEF
import koin.apiModule
import koin.dbModule
import koin.networkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.context.startKoin
import theme.WindowSize
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.io.File
import kotlin.math.max

fun main() {
    application {
        startKoin {
            modules(networkModule, apiModule, dbModule)
        }
        Window(onCloseRequest = ::exitApplication, title = "Fetch 2") {
            var restartRequired by remember { mutableStateOf(false) }
            var downloading by remember { mutableStateOf(0F) }
            var initialized by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    KCEF.init(builder = {
                        installDir(File("kcef-bundle"))
                        progress {
                            onDownloading {
                                downloading = max(it, 0F)
                            }
                            onInitialized {
                                initialized = true
                            }
                        }
                        settings {
                            cachePath = File("cache").absolutePath
                            release("jbr-release-17.0.10b1087.23")
                            windowlessRenderingEnabled = true
                            remoteDebuggingPort = 8080
                        }
                        args("allow-running-insecure-content", "-remote-allow-origins=*")
                    }, onError = {
                        it?.printStackTrace()
                    }, onRestartRequired = {
                        restartRequired = true
                    })
                }
            }

            if (restartRequired) {
                Text(text = "Restart required.")
            } else {
                if (initialized) {
                    App(rememberWindowSize())
                } else {
                    Text(text = "Downloading $downloading%")
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    KCEF.disposeBlocking()
                }
            }
        }
    }
}

@Composable
private fun FrameWindowScope.rememberWindowSize(): WindowSize {
    var windowMetrics by remember {
        mutableStateOf(this.window.size)
    }

    val windowDpSize = with(LocalDensity.current) {
        windowMetrics.width.toDp()
    }

    LaunchedEffect(windowMetrics) {
        val componentListener = object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                windowMetrics = window.size
            }
        }
        window.addComponentListener(componentListener)
    }

    return WindowSize.basedOnWidth(windowDpSize)

}