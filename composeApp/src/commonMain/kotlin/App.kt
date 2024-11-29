import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import api.IProxyApi
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import screens.allshows.AllShowsTab
import screens.downloads.DownloadsTab
import screens.frontpage.HomeTab
import screens.popular.PopularTab
import screens.schedule.ScheduleTab
import screens.search.SearchTab
import screens.settings.SettingsTab
import screens.trending.TrendingTab
import theme.KickassAnimeTheme
import theme.LocalWindowSize
import theme.WindowSize

@Composable
@Preview
fun App(windowSize: WindowSize) {
    KoinContext {
        KickassAnimeTheme(windowSize) {
            AppInternal()
        }
    }
}

@Composable
private fun AppInternal(proxy: IProxyApi = koinInject()) {
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(proxy) {
        proxy.getReachableProxyFromList()
        isLoading = false
    }
    if (!isLoading) {
        KickassApp()
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun KickassApp() {
    TabNavigator(HomeTab) {
        Scaffold(
            bottomBar = {
                if (LocalWindowSize.current <= WindowSize.COMPACT) {
                    val scrollState = rememberScrollState()
                    BottomNavigation(
                        contentColor = MaterialTheme.colors.primaryVariant,
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.horizontalScroll(scrollState)
                            ) {
                                Tabs()
                            }
                        }
                    }
                }
            }
        ) {
            Row {
                if (LocalWindowSize.current > WindowSize.COMPACT) {
                    val scrollState = rememberScrollState()
                    NavigationRail(
                        contentColor = MaterialTheme.colors.primaryVariant
                    ) {
                        Column(
                            modifier = Modifier.verticalScroll(scrollState)
                        ) {
                            Tabs()
                        }
                    }
                }
                CurrentTab()
            }
        }
    }
}

@Composable
private fun Tabs() {
    TabNavigationItem(HomeTab)
    TabNavigationItem(SearchTab)
    TabNavigationItem(ScheduleTab)
    TabNavigationItem(PopularTab)
    TabNavigationItem(TrendingTab)
    TabNavigationItem(AllShowsTab)
    TabNavigationItem(DownloadsTab)
    TabNavigationItem(SettingsTab)
}

@Composable
private fun TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    NavigationRailItem(
        unselectedContentColor = Color.White,
        selectedContentColor = MaterialTheme.colors.onPrimary,
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        label = { Text(tab.options.title) },
        icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
    )
}