package screens.anime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import api.IKickassAnimeApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.otaku.fetch2.AnimeLanguage
import com.otaku.fetch2.Database
import model.EpisodeTile
import org.koin.compose.koinInject
import screens.components.RetryingImage
import theme.LocalCellSizes
import theme.LocalSpacings

class AnimePage(private val tileData: EpisodeTile) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        Scaffold(
            topBar = {
                IconButton(onClick = {
                    navigator?.pop()
                }) {
                    Icon(painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),contentDescription = "back")
                }
            }
        ) {
            MainScreen()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MainScreen(database: Database = koinInject(), animeApi: IKickassAnimeApi = koinInject()) {
        val language: AnimeLanguage
        val vm = AnimeViewModel()
        LaunchedEffect(Unit) {
            vm.fetchLanguages(tileData.animeSlug, animeApi, database)
        }
        val languages = vm.getLanguages(tileData.animeSlug, database).collectAsState(null).value
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalSpacings.current.sm)
            ) {
                Card(modifier = Modifier.size(LocalCellSizes.current.xl).padding(LocalSpacings.current.sm)) {
                    RetryingImage(tileData.imageUrl)
                }
                Text(text = tileData.title ?: "", style = MaterialTheme.typography.h4)
            }
            LazyRow {
                if (languages != null) {
                    items(languages) {
                        FilterChip(false, onClick = {}) {
                            Text(it.language)
                        }
                    }
                } else {
                    item {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

}