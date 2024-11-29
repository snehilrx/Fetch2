package screens.frontpage

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import api.IKickassAnimeApi
import api.impl.ProxyApi
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.otaku.fetch2.Database
import compose.icons.TablerIcons
import compose.icons.tablericons.Home
import fetch2.composeapp.generated.resources.Res
import fetch2.composeapp.generated.resources.all
import fetch2.composeapp.generated.resources.dub
import fetch2.composeapp.generated.resources.home
import fetch2.composeapp.generated.resources.retry
import fetch2.composeapp.generated.resources.sub
import model.ITileData
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import screens.components.ITileList
import screens.components.RetryingImage
import theme.LocalCellSizes
import theme.LocalSpacings

object HomeTab : Tab {
    @Composable
    override fun Content() {
        Navigator(FrontPage())
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.home)
            val icon = rememberVectorPainter(TablerIcons.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    class FrontPage : Screen {
        @OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
        @Composable
        fun FrontPage(
        ) {
            var tabIndex by remember { mutableStateOf(0) }

            var scrollStateVertical by remember { mutableStateOf(1f) }
            val pagerState = rememberPagerState(pageCount = { AnimeType.entries.size })
                    val maxScroll = LocalSpacings.current.xl.value
            val connection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        val newScrollStateVertical = scrollStateVertical + available.y

                        return if (newScrollStateVertical in 0f..maxScroll) {
                            val scrollAmount = newScrollStateVertical.coerceIn(0f, maxScroll)
                            scrollStateVertical =
                                1f - (scrollAmount / maxScroll + 0.5f).coerceIn(0f..1f)
                            Offset.Zero
                        } else {
                            super.onPreScroll(available, source)
                        }
                    }
                }
            }
            LaunchedEffect(tabIndex) {
                pagerState.animateScrollToPage(tabIndex)
                snapshotFlow { pagerState.currentPage }.collect { currentPage ->
                    tabIndex = currentPage
                }
            }
            Column {
                val url = "${ProxyApi.getCachedEndpoint()}/img/logo.png"
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.secondary)
                ) {
                    Row(
                        modifier = Modifier.wrapContentSize().padding(LocalSpacings.current.m)
                            .align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RetryingImage(
                            url,
                            arrayOf(url),
                            Modifier.width(LocalCellSizes.current.xxl)
                        )
                    }
                }
                TabRow(selectedTabIndex = tabIndex) {
                    AnimeType.entries.forEachIndexed { index, it ->
                        Tab(text = { Text(stringResource(it.value)) },
                            selected = tabIndex == index,
                            onClick = {
                                tabIndex = index
                            }
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .nestedScroll(connection)
                ) {
                    Page(it)
                }
            }
        }

        @Composable
        private fun Page(
            pageNo: Int,
            database: Database = koinInject(),
            iKickassAnimeApi: IKickassAnimeApi = koinInject()
        ) {
            val frontPageViewModel = remember {
                FrontPageViewModel()
            }
            val flow = remember {
                derivedStateOf {
                    when (AnimeType.entries[pageNo]) {
                        AnimeType.SUB -> frontPageViewModel.sub(database, iKickassAnimeApi)
                        AnimeType.ALL -> frontPageViewModel.all(database, iKickassAnimeApi)
                        AnimeType.DUB -> frontPageViewModel.dub(database, iKickassAnimeApi)
                    }
                }
            }
            val recent = flow.value.collectAsLazyPagingItems()
            ITileList(recent)
        }

        @Composable
        override fun Content() {
            FrontPage()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StateIndicator(state: LoadState, items: LazyPagingItems<out ITileData>) {
    Column(verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
        when (state) {
            is LoadState.Error -> {
                Text(state.error.message ?: "Something went wrong")
                Button(onClick = {
                    items.retry()
                }){
                    Text(stringResource(Res.string.retry))
                }
            }
            is LoadState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier.width(20.dp).height(20.dp)
                )

            is LoadState.NotLoading -> {}
        }
    }
}


@OptIn(ExperimentalResourceApi::class)
private enum class AnimeType(val value: StringResource) {
    SUB(Res.string.sub), DUB(Res.string.dub), ALL(Res.string.all);
}