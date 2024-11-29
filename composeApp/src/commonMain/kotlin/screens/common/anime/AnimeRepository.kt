package screens.common.anime

import Constants
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.sqldelight.Query
import app.cash.sqldelight.paging3.QueryPagingSource
import com.otaku.fetch2.Anime
import com.otaku.fetch2.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import model.AnimeResponse
import model.AnimeTile
import model.ApiState

class AnimeRepository(
    private val database: Database,
    private val networkFetch: suspend (Long) -> ApiState<AnimeResponse?>,
    private val insert: (id: Long, pageNo: Long, animeSlug: String) -> Unit,
    private val query: (
        limit: Long,
        offset: Long,
        mapper: (
            animeSlug: String,
            name: String?,
            description: String?,
            image: String?,
            status: String?,
            type: String?,
            rating: String?,
            favourite: Long?,
            year: Long?,
            page: Long,
        ) -> AnimeTile,
    ) -> Query<AnimeTile>
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getPager() = Pager(
        config = PagingConfig(
            pageSize = Constants.NETWORK_PAGE_SIZE,
            enablePlaceholders = true,
        ),
        remoteMediator = AnimeRemoteMediator(
            database,
            networkFetch,
            insert
        )
    ) {
        val mapper = { limit: Long, offset: Long -> query(limit, offset) { animeSlug: String,
                                                                   name: String?,
                                                                   description: String?,
                                                                   image: String?,
                                                                   status: String?,
                                                                   type: String?,
                                                                   rating: String?,
                                                                   favourite: Long?,
                                                                   year: Long?,
                                                                   page: Long? ->
                AnimeTile(
                    animeEntity = Anime(
                        animeSlug, name, description, image, status, type, rating, favourite, year
                    ),
                    pageNo = page
                )
            }
        }
        QueryPagingSource(
            countQuery = database.recentQueries.countAll(),
            transacter = database.recentQueries,
            context = Dispatchers.IO, mapper
        )
    }
}