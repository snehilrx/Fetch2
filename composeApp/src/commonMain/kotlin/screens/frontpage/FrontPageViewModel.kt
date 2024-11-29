package screens.frontpage

import androidx.paging.PagingData
import androidx.paging.cachedIn
import api.IKickassAnimeApi
import com.otaku.fetch2.Database
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.Flow
import model.EpisodeTile

class FrontPageViewModel : KMMViewModel() {

    private lateinit var allFlow : Flow<PagingData<EpisodeTile>>
    private lateinit var dubFlow : Flow<PagingData<EpisodeTile>>
    private lateinit var subFlow : Flow<PagingData<EpisodeTile>>

    fun all( database: Database, kickassAnimeApi: IKickassAnimeApi): Flow<PagingData<EpisodeTile>> {
        if (!this::allFlow.isInitialized) {
            this.allFlow = FrontPageRepository(database, kickassAnimeApi)
                .getRecentPager().flow.cachedIn(viewModelScope.coroutineScope)
        }
        return this.allFlow
    }
    fun dub( database: Database, kickassAnimeApi: IKickassAnimeApi): Flow<PagingData<EpisodeTile>> {
        if (!this::dubFlow.isInitialized) {
            this.dubFlow = FrontPageRepository(database, kickassAnimeApi)
                .getDubPager().flow.cachedIn(viewModelScope.coroutineScope)
        }
        return this.dubFlow
    }

    fun sub( database: Database, kickassAnimeApi: IKickassAnimeApi): Flow<PagingData<EpisodeTile>> {
        if (!this::subFlow.isInitialized) {
            this.subFlow = FrontPageRepository(database, kickassAnimeApi)
                .getSubPager().flow.cachedIn(viewModelScope.coroutineScope)
        }
        return this.subFlow
    }
}