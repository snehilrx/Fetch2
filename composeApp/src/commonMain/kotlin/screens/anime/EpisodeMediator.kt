package screens.anime

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import api.IKickassAnimeApi
import kotlinx.datetime.Clock
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import com.otaku.fetch2.Database
import kotlinx.datetime.Instant
import model.ApiState
import utils.Utils

data class EpisodeTile(
    val pageNo: Int,
    var episodeNumber: Double? = null,
    var thumbnail: String? = null,
    val slug: String? = null,
    val title: String? = null,
    val duration: Long? = null
) {
    fun readableDuration(): String {
        return "${duration?.toDuration(DurationUnit.MILLISECONDS)?.inWholeMinutes} min"
    }
}

@OptIn(ExperimentalPagingApi::class)
class EpisodeRemoteMediator(
    private val animeSlug: String,
    private val language: String,
    private val startPage: Int,
    private val api: IKickassAnimeApi,
    private val database: Database
) : RemoteMediator<Int, EpisodeTile>() {

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, EpisodeTile>
    ): MediatorResult {
        return try {
            val endPaging = MediatorResult.Success(endOfPaginationReached = true)
            val continuePaging = MediatorResult.Success(endOfPaginationReached = false)
            val loadKey = when (loadType) {
                LoadType.REFRESH -> startPage
                LoadType.PREPEND -> return endPaging
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()?.pageNo ?: startPage
                    lastItem.minus(1)
                }
            }
            if (loadKey <= 0) {
                return endPaging
            }

            return when (val response = api.getEpisodes(animeSlug, language, loadKey)) {
                is ApiState.COMPLETED -> {
                    if (response.value?.result.isNullOrEmpty() || response.value == null) {
                        endPaging
                    } else {
                        Utils.saveEpisodePage(animeSlug, language, response.value, database, loadKey)
                        continuePaging
                    }
                }
                is ApiState.ERROR -> {
                    MediatorResult.Error(Throwable(response.message))
                }
                else -> {
                    MediatorResult.Error(Throwable())
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val lastUpdate = database.lastUpdatedQueries.getLastUpdated().executeAsOneOrNull()?.createdDate?.let {
            Instant.parse(
                it
            )
        }
        return if (lastUpdate?.minus( Clock.System.now())?.isPositive() == true) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
}
