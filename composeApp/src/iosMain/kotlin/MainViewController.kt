import androidx.compose.ui.window.ComposeUIViewController
import koin.apiModule
import koin.dbModule
import koin.networkModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    startKoin {
        modules(networkModule, apiModule, dbModule)
    }
}