package screens.common.anime

import androidx.paging.PagingData
import androidx.paging.cachedIn
import api.IKickassAnimeApi
import com.otaku.fetch2.AllAnime
import com.otaku.fetch2.Database
import com.otaku.fetch2.PopularAnime
import com.otaku.fetch2.TrendingAnime
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.Flow
import model.AnimeTile

class CommonAnimeViewModel : KMMViewModel() {

    private lateinit var allFlow : Flow<PagingData<AnimeTile>>
    private lateinit var popularFlow : Flow<PagingData<AnimeTile>>
    private lateinit var trendingFlow : Flow<PagingData<AnimeTile>>

    fun all(database: Database, kickassAnimeApi: IKickassAnimeApi): Flow<PagingData<AnimeTile>> {
        if (!this::allFlow.isInitialized) {
            val commonRepository = AnimeRepository(database, kickassAnimeApi::getAllAnimes, { id, page, slug ->
                database.animeQueries.insertAllAnime(AllAnime(id, slug, page))
            }, database.animeQueries::getAllAnime)
            this.allFlow = commonRepository.getPager().flow.cachedIn(viewModelScope.coroutineScope)
        }
        return this.allFlow
    }

    fun popular(database: Database, kickassAnimeApi: IKickassAnimeApi): Flow<PagingData<AnimeTile>> {
        if (!this::popularFlow.isInitialized) {
            val commonRepository = AnimeRepository(database, kickassAnimeApi::getPopularAnimes, { id, page, slug ->
                database.animeQueries.insertPopular(PopularAnime(id, slug, page))
            }, database.animeQueries::getPopularAnime)
            this.popularFlow = commonRepository.getPager().flow.cachedIn(viewModelScope.coroutineScope)
        }
        return this.popularFlow
    }

    fun trending(database: Database, kickassAnimeApi: IKickassAnimeApi): Flow<PagingData<AnimeTile>> {
        if (!this::trendingFlow.isInitialized) {
            val commonRepository = AnimeRepository(database, kickassAnimeApi::getTrendingAnimes, { id, page, slug ->
                database.animeQueries.insertTrending(TrendingAnime(id, slug, page))
            }, database.animeQueries::getTrendingAnime)
            this.trendingFlow = commonRepository.getPager().flow.cachedIn(viewModelScope.coroutineScope)
        }
        return this.trendingFlow
    }
}