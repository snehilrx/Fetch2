package screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.LazyPagingItems
import cafe.adriel.voyager.navigator.LocalNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import fetch2.composeapp.generated.resources.Res
import fetch2.composeapp.generated.resources.compose_multiplatform
import fetch2.composeapp.generated.resources.download
import fetch2.composeapp.generated.resources.info
import fetch2.composeapp.generated.resources.thumbnail
import model.EpisodeTile
import model.ITileData
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.anime.AnimePage
import screens.episode.EpisodePage
import screens.frontpage.StateIndicator
import theme.LocalCellSizes
import theme.LocalSpacings
import theme.LocalWindowSize
import theme.WindowSize


@OptIn(ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class, ExperimentalResourceApi::class
)
@Preview
@Composable
fun RecentEpisode(tileData: ITileData) {
    val cornerShape = RoundedCornerShape(12.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val navigator = LocalNavigator.current
    val hoverState = interactionSource.collectIsHoveredAsState()
    val animatedPadding by animateDpAsState(
        if (hoverState.value) {
            0.dp
        } else {
            18.dp
        },
        label = "padding"
    )
    Card(
        modifier = Modifier
            .padding(animatedPadding)
            .height(LocalCellSizes.current.xxxl)
            .scale(if (hoverState.value) {
                1.12f
            } else {
                1f
            })
            .fillMaxSize()
            .hoverable(interactionSource),
        shape = cornerShape,
        onClick = {
            if (tileData is EpisodeTile) {
                navigator?.push(EpisodePage(tileData))
            }
        },
        elevation = LocalSpacings.current.xxxxs
    ) {
        RetryingImage(tileData.imageUrl)
        var sizeImage by remember { mutableStateOf(IntSize.Zero) }

        val gradient = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startY = sizeImage.height.toFloat()/3,  // 1/3
            endY = sizeImage.height.toFloat()
        )


        Box(modifier = Modifier.fillMaxSize().background(gradient))
        Column  (
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .onGloballyPositioned {
                    sizeImage = it.size
                },
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = tileData.title ?: "",
                modifier = Modifier
                    .padding(LocalSpacings.current.xs)
                    .basicMarquee(),
                maxLines = 1,
                color = Color.White,
                style = MaterialTheme.typography.caption,
                lineHeight = 20.sp
            )
            Row {
                tileData.tags.forEach {tag ->
                    Chip(
                        modifier = Modifier
                            .padding(horizontal = LocalSpacings.current.xxs, vertical = LocalSpacings.current.xxxs),
                        onClick = {}
                    ) {
                        Text(
                            text = tag,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.caption,
                            lineHeight = 16.sp)
                    }
                }
            }
            AnimatedVisibility(hoverState.value){
                Row(horizontalArrangement = Arrangement.spacedBy(LocalSpacings.current.xxxs),
                    modifier = Modifier.padding(LocalSpacings.current.xxxs)) {
                    Button(shape = cornerShape, onClick = {
                        if (tileData is EpisodeTile) {
                            navigator?.push(AnimePage(tileData))
                        }
                    }) {
                        Text(text = stringResource(Res.string.info), style = MaterialTheme.typography.caption)
                    }
                    Button(shape = cornerShape, onClick = {}) {
                        Text(text = stringResource(Res.string.download), style = MaterialTheme.typography.caption)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RetryingImage(
    url: String?,
    urls: Array<String> = arrayOf(
        "${url}-hq.webp", "${url}-hq.jpg", "${url}-hq.jpeg",
        "${url}-hq.webp", "${url}-hq.jpg", "${url}-hq.jpeg"
    ),
    modifier: Modifier = Modifier.fillMaxSize()
) {

    var urlIndex by remember { mutableStateOf(0) }
    CoilImage(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium),
        imageModel = { urls[urlIndex] },
        imageOptions = ImageOptions(
            contentDescription = stringResource(resource = Res.string.thumbnail),
            contentScale = ContentScale.Crop
        ),
        loading = {
            Box(modifier = Modifier.matchParentSize()) {
                Image(painter = painterResource(resource = Res.drawable.compose_multiplatform), contentDescription = null)
            }
        },
        failure = {
            if (urlIndex < urls.size-1) {
                urlIndex++
            } else {
                Text(text = "image request failed. ${it.reason?.message} - $url")
            }
        }
    )
}

@Composable
fun Adaptive(content: @Composable () -> Unit){
    if (LocalWindowSize.current > WindowSize.COMPACT) {
        Row {
            content()
        }
    } else {
        Column {
            content()
        }
    }
}

@Composable
fun ITileList(items: LazyPagingItems<out ITileData>) {
    val columns = GridCells.Adaptive(
        if (LocalWindowSize.current < WindowSize.MEDIUM) {
            LocalCellSizes.current.xxl
        } else {
            LocalCellSizes.current.xxxl
        }
    )
    LazyVerticalGrid(
        columns = columns,
        contentPadding = PaddingValues(LocalSpacings.current.xs),
    ) {
        item(span = {
            GridItemSpan(maxCurrentLineSpan)
        }) {
            StateIndicator(items.loadState.refresh, items)
        }
        item(span = {
            GridItemSpan(maxCurrentLineSpan)
        }) {
            StateIndicator(items.loadState.prepend, items)
        }
        items(items.itemCount) { index ->
            items[index]?.let {
                RecentEpisode(it)
            }
        }
        item(
            span = {
                GridItemSpan(maxCurrentLineSpan)
            }
        ) {
            StateIndicator(items.loadState.append, items)
        }
        item {
            Column(Modifier.height(LocalCellSizes.current.xxl)) {

            }
        }
    }
}

@Composable
fun AppBarWithIcon(icon: ImageVector, text: String) {
    TopAppBar (
        contentPadding = PaddingValues(horizontal = LocalSpacings.current.sm, vertical = LocalSpacings.current.xs),
    ) {
        Row(modifier = Modifier.align(Alignment.CenterVertically), horizontalArrangement = Arrangement.spacedBy(LocalSpacings.current.sm)) {
            Icon(painter = rememberVectorPainter(icon), contentDescription = text, modifier = Modifier.align(Alignment.CenterVertically))
            Text(text, modifier = Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.h6)
        }
    }
}