package screens.common.anime

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import asAnimeEntity
import coil3.network.HttpException
import com.otaku.fetch2.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import model.AnimeResponse
import model.AnimeTile
import model.ApiState
import model.ApiStateWithLoading
import okio.IOException

@OptIn(ExperimentalPagingApi::class)
class AnimeRemoteMediator(
    private val database: Database,
    private val networkFetch: suspend (Long) -> ApiState<AnimeResponse?>,
    private val save: (id: Long, pageNo: Long, animeSlug: String) -> Unit,
    ) : RemoteMediator<Int, AnimeTile>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AnimeTile>
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

            return when(val response = networkFetch(loadKey)) {
                is ApiState.COMPLETED -> {
                    if (response.value == null || response.value.result.isNullOrEmpty()) {
                        endPaging
                    } else {
                        val animeItems = response.value.result?.filterNotNull()
                        withContext(Dispatchers.IO) {
                            database.transaction {
                                animeItems?.forEachIndexed { index, it ->
                                    database.animeQueries.insert(it.asAnimeEntity())
                                    save(index.toLong(),  loadKey, it.slug!!)
                                }
                            }
                        }
                        continuePaging
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

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH
}