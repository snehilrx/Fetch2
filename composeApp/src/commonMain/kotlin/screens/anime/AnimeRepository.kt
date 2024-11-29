package screens.anime

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import api.IKickassAnimeApi
import app.cash.sqldelight.paging3.QueryPagingSource
import com.otaku.fetch2.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class AnimeRepository {
    @OptIn(ExperimentalPagingApi::class)
    fun getEpisodesPager(
        animeSlug: String,
        language: String,
        api: IKickassAnimeApi,
        database: Database
    ) = Pager(
        config = PagingConfig(
            pageSize = Constants.NETWORK_PAGE_SIZE,
            enablePlaceholders = true,
        ),
        remoteMediator = EpisodeRemoteMediator(
            animeSlug,
            language,
            1,
            api,
            database
        ),
    ) {
        val mapper = { limit: Long, offset: Long ->
            database.episodeQueries.getAllEpisodes(animeSlug, language, limit, offset, mapper = {
                    pageNo: Long?,
                    episodeNumber: Double?,
                    thumbnail: String?,
                    slug: String,
                    title: String?,
                    duration: Long?, ->
                EpisodeTile(
                    title = title,
                    episodeNumber = episodeNumber,
                    pageNo = pageNo?.toInt() ?: 0,
                    thumbnail = thumbnail,
                    duration = duration,
                )
            })
        }
        QueryPagingSource(
            countQuery = database.episodeQueries.countAll(animeSlug),
            transacter = database.episodeQueries,
            context = Dispatchers.IO, mapper
        )
    }
}