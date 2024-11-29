package screens.anime

import androidx.paging.cachedIn
import api.IKickassAnimeApi
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.otaku.fetch2.AnimeLanguage
import com.otaku.fetch2.Database
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.Flow
import model.ApiState
import model.BaseApiResponse

class AnimeViewModel : KMMViewModel() {
    fun info(animeSlug: String, database: Database) =
        database.animeQueries.getAnime(animeSlug).executeAsOneOrNull()

    suspend fun fetchLanguages(animeSlug: String, animeApi: IKickassAnimeApi, database: Database) : ApiState<*> {
        val language = animeApi.getLanguage(animeSlug)
        if (language is ApiState.COMPLETED) {
            val animeLanguages = language.value?.result?.filterNotNull()?.map {
                AnimeLanguage(it, animeSlug)
            }
            database.transaction {
                animeLanguages?.forEach {
                    database.animeQueries.insertLanguage(it)
                }
            }
        }
        return language
    }

    fun getLanguages(animeSlug: String, database: Database) : Flow<List<AnimeLanguage>> = database.animeQueries.getAnimeLanguage(animeSlug)
        .asFlow().mapToList(viewModelScope.coroutineScope.coroutineContext)

    fun all(
        animeSlug: String,
        language: String,
        kickassAnimeApi: IKickassAnimeApi,
        database: Database
    ) = AnimeRepository().getEpisodesPager(
        animeSlug,
        language,
        kickassAnimeApi,
        database
    ).flow.cachedIn(viewModelScope.coroutineScope)

}