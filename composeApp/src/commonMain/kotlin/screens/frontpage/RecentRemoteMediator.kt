package screens.frontpage

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import coil3.network.HttpException
import com.otaku.fetch2.Database
import model.EpisodeTile
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import model.ApiState
import model.BaseApiResponse
import model.Recent
import model.RecentApiResponse
import okio.IOException

@OptIn(ExperimentalPagingApi::class)
class RecentMediator<T>(
    private val database: Database,
    private val networkFetch: suspend (Long) -> ApiState<out BaseApiResponse<T>?>,
    private val save: suspend (List<T>, Database, Long) -> Unit
) : RemoteMediator<Int, EpisodeTile>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeTile>
    ): MediatorResult {
        return try {
            val endPaging = MediatorResult.Success(endOfPaginationReached = true)
            val continuePaging = MediatorResult.Success(endOfPaginationReached = false)
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return endPaging
                LoadType.APPEND -> {
                    state.lastItemOrNull()?.pageNo?.plus(1L) ?: 1L
                }
            }

            when(val response = networkFetch(loadKey)) {
                is ApiState.COMPLETED -> {
                    return if (response.value?.result?.isNotEmpty() == true) {
                        response.value.result?.filterNotNull()?.let { save(it, database, loadKey) }
                        continuePaging
                    } else {
                        endPaging
                    }
                }
                is ApiState.ERROR -> {
                    MediatorResult.Error(Throwable(response.message))
                }
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val lastUpdate = database.lastUpdatedQueries.getLastUpdated().executeAsList()
            .lastOrNull()?.createdDate?.let {
                LocalDateTime.parse(it.substring(0, 19)).toInstant(TimeZone.UTC)
        }
        return if (lastUpdate?.minus( Clock.System.now())?.isPositive() == true) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
}