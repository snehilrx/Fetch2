package screens.allshows

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
import compose.icons.tablericons.MoodCrazyHappy
import fetch2.composeapp.generated.resources.Res
import fetch2.composeapp.generated.resources.all_shows
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import screens.common.anime.CommonAnimeViewModel
import screens.components.ITileList

object AllShowsTab : Tab {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                TopAppBar {
                    Text(stringResource(Res.string.all_shows))
                }
            }
        ) {
            AllTab()
        }
    }

    @Composable
    private fun AllTab(
        database: Database = koinInject(),
        iKickassAnimeApi: IKickassAnimeApi = koinInject()
    ) {
        val viewModel = CommonAnimeViewModel()
        val flow = viewModel.all(database, iKickassAnimeApi)
        val allItems = flow.collectAsLazyPagingItems()
        ITileList(allItems)
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.all_shows)
            val icon = rememberVectorPainter(TablerIcons.MoodCrazyHappy)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}