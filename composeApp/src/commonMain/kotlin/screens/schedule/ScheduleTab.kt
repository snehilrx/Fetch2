package screens.schedule

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import api.IKickassAnimeApi
import api.impl.ProxyApi
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import compose.icons.TablerIcons
import compose.icons.tablericons.Calendar
import compose.icons.tablericons.Flame
import fetch2.composeapp.generated.resources.Res
import fetch2.composeapp.generated.resources.calender
import fetch2.composeapp.generated.resources.popular_shows
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import model.ApiState
import model.ApiStateWithLoading
import model.ScheduleApiResponse
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import screens.components.Adaptive
import screens.components.AppBarWithIcon
import screens.components.RetryingImage
import theme.LocalCellSizes
import theme.LocalSpacings

object ScheduleTab : Tab {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                AppBarWithIcon(
                    TablerIcons.Calendar,
                    stringResource(Res.string.calender)
                )
            }
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                ScheduleScreen()
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun ScheduleScreen(api: IKickassAnimeApi = koinInject()) {
        var series: ApiStateWithLoading<List<ScheduleApiResponse>> by remember { mutableStateOf(ApiStateWithLoading.LOADING()) }
        LaunchedEffect(Unit) {
            val schedule = api.getSchedule()
            series = when(schedule) {
                is ApiState.ERROR -> {
                    ApiStateWithLoading.ERROR(schedule.message)
                }
                is ApiState.COMPLETED -> {
                    ApiStateWithLoading.COMPLETED(schedule.value)
                }
            }
        }

        when (series) {
            is ApiStateWithLoading.COMPLETED -> {
                ShowCalendar((series as ApiStateWithLoading.COMPLETED<List<ScheduleApiResponse>>).value)
            }

            is ApiStateWithLoading.ERROR -> {
                Text((series as ApiStateWithLoading.ERROR<List<ScheduleApiResponse>>).message)
            }

            is ApiStateWithLoading.LOADING -> {
                CircularProgressIndicator()
            }
        }
    }

    @Composable
    private fun ShowCalendar(value: List<ScheduleApiResponse>) {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        var selected by remember { mutableStateOf(today.dayOfWeek.ordinal) }
        var map: Map<DayOfWeek, List<ScheduleApiResponse>> by remember { mutableStateOf(HashMap()) }
        LaunchedEffect(value) {
            val pairList = value.map {
                Instant.fromEpochMilliseconds(it.releaseTime ?: 0).toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ) to it
            }.filter { it.first.year == today.year }
                .sortedBy { it.first }
            map = pairList.groupBy({ it.first.dayOfWeek }, { it.second })
            selected = 0
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Adaptive {
                if (map.isNotEmpty()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TabRow(selectedTabIndex = selected) {
                            DayOfWeek.entries.forEachIndexed { index, it ->
                                Tab(text = { Text(it.name) },
                                    selected = selected == index,
                                    onClick = { selected = index }
                                )
                            }
                        }
                    }
                    val columns = GridCells.Adaptive(LocalCellSizes.current.xxxxl)
                    LazyVerticalGrid(
                        columns = columns,
                        contentPadding = PaddingValues(LocalSpacings.current.sm),
                    ) {
                        map[DayOfWeek.entries[selected]]?.let { list ->
                            items(list) {
                                Item(it)
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun Item(item: ScheduleApiResponse) {
        val cornerShape = RoundedCornerShape(8.dp)
        val interactionSource = remember { MutableInteractionSource() }
        val navigator = LocalNavigator.current
        val hoverState = interactionSource.collectIsHoveredAsState()

        val url =
            buildString {
                append("${ProxyApi.getCachedEndpoint()}/image/poster/${item.poster?.hq}")
                deleteRange(length - 3, length)
            }

        val animatedPadding by animateDpAsState(
            if (hoverState.value) {
                0.dp
            } else {
                LocalSpacings.current.sm
            },
            label = "padding"
        )
        Card(
            modifier = Modifier
                .padding(animatedPadding)
                .animateContentSize()
                .height(LocalCellSizes.current.xl)
                .scale(
                    if (hoverState.value) {
                        1.12f
                    } else {
                        1f
                    }
                )
                .fillMaxSize()
                .hoverable(interactionSource),
            shape = cornerShape,
            onClick = {

            },
            elevation = LocalSpacings.current.xxxxs
        ) {
            Row() {
                RetryingImage(
                    url,
                    modifier = Modifier.fillMaxHeight().wrapContentWidth()
                )
                Column(
                    modifier = Modifier.padding(LocalSpacings.current.sm),
                    verticalArrangement = Arrangement.spacedBy(LocalSpacings.current.sm)
                ) {
                    Text(item.titleEn ?: "", maxLines = 2)
                    Text(
                        Instant.fromEpochMilliseconds(item.releaseTime!!)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).time.toString()
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.calender)
            val icon = rememberVectorPainter(TablerIcons.Calendar)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}