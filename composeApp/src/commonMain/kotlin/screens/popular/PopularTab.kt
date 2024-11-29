package screens.popular

import androidx.compose.material.Scaffold
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
import fetch2.composeapp.generated.resources.Res
import fetch2.composeapp.generated.resources.popular_shows
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import screens.common.anime.CommonAnimeViewModel
import screens.components.AppBarWithIcon
import screens.components.ITileList

object PopularTab : Tab {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                AppBarWithIcon(
                    TablerIcons.Flame,
                    stringResource(Res.string.popular_shows)
                )
            }
        ) {
            PopularTab()
        }
    }

    @Composable
    private fun PopularTab(
        database: Database = koinInject(),
        iKickassAnimeApi: IKickassAnimeApi = koinInject()
    ) {
        val viewModel = CommonAnimeViewModel()
        val flow = viewModel.popular(database, iKickassAnimeApi)
        val popularItems = flow.collectAsLazyPagingItems()
        ITileList(popularItems)
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.popular_shows)
            val icon = rememberVectorPainter(TablerIcons.Flame)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}