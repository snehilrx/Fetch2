package screens.trending

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import api.IKickassAnimeApi
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.otaku.fetch2.Database
import compose.icons.TablerIcons
import compose.icons.tablericons.Flame
import compose.icons.tablericons.TrendingUp
import fetch2.composeapp.generated.resources.Res
import fetch2.composeapp.generated.resources.popular_shows
import fetch2.composeapp.generated.resources.trending
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import screens.common.anime.CommonAnimeViewModel
import screens.components.AppBarWithIcon
import screens.components.ITileList

object TrendingTab : Tab {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                AppBarWithIcon(
                    TablerIcons.TrendingUp,
                    stringResource(Res.string.trending)
                )
            }
        ) {
            TrendingTab()
        }
    }

    @Composable
    private fun TrendingTab(
        database: Database = koinInject(),
        iKickassAnimeApi: IKickassAnimeApi = koinInject()
    ) {
        val viewModel = CommonAnimeViewModel()
        val flow = viewModel.trending(database, iKickassAnimeApi)
        val trendingItems = flow.collectAsLazyPagingItems()
        ITileList(trendingItems)
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.trending)
            val icon = rememberVectorPainter(TablerIcons.TrendingUp)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}