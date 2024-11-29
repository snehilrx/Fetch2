package screens.frontpage

import Constants
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import api.IKickassAnimeApi
import app.cash.sqldelight.paging3.QueryPagingSource
import com.otaku.fetch2.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import model.EpisodeTile
import utils.Utils

class FrontPageRepository(
    private val database: Database,
    private val kickassAnimeApi: IKickassAnimeApi
) {

    private val tileMapper = { title: String?,
                       animeSlug: String,
                       language: String?,
                       episodeSlug: String,
                       image: String?,
                       episodeNumber: Double?,
                       pageNo: Long? ->
        EpisodeTile(
            title = title,
            animeSlug = animeSlug,
            language = language,
            episodeSlug = episodeSlug,
            image = image,
            episodeNumber = episodeNumber,
            pageNo = pageNo
        )
    }
    @OptIn(ExperimentalPagingApi::class)
    fun getRecentPager(): Pager<Int, EpisodeTile> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            remoteMediator = RecentMediator(
                database,
                kickassAnimeApi::getFrontPageAnimeList,
                Utils::saveRecent
            ),
        ) {
            val mapper = { limit: Long, offset: Long ->
                database.recentQueries.getAll(limit, offset, mapper = tileMapper)
            }
            QueryPagingSource(
                countQuery = database.recentQueries.countAll(),
                transacter = database.recentQueries,
                context = Dispatchers.IO, mapper
            )
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getSubPager() = Pager(
        config = PagingConfig(
            pageSize = Constants.NETWORK_PAGE_SIZE,
            enablePlaceholders = true,
        ),
        remoteMediator = RecentMediator(
            database,
            kickassAnimeApi::getFrontPageAnimeListSub,
            Utils::saveRecent
        ),
    ) {
        val mapper = { limit: Long, offset: Long ->
            database.recentQueries.getAllSubbed(limit, offset, mapper = tileMapper)
        }
        QueryPagingSource(
            countQuery = database.recentQueries.countAll(),
            transacter = database.recentQueries,
            context = Dispatchers.IO, mapper
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getDubPager() = Pager(
        config = PagingConfig(
            pageSize = Constants.NETWORK_PAGE_SIZE,
            enablePlaceholders = true
        ),
        remoteMediator = RecentMediator(
            database,
            kickassAnimeApi::getFrontPageAnimeListDub,
            Utils::saveRecent
        ),
    ) {
        val mapper = { limit: Long, offset: Long ->
            database.recentQueries.getAllDubbed(limit, offset, mapper = tileMapper)
        }
        QueryPagingSource(
            countQuery = database.recentQueries.countAll(),
            transacter = database.recentQueries,
            context = Dispatchers.IO, mapper
        )
    }

}
