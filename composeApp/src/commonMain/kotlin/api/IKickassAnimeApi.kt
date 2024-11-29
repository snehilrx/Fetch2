package api

import model.AnimeResponse
import model.ApiState
import model.BaseApiResponse
import model.EpisodeApiResponse
import model.EpisodesResponse
import model.Filters
import model.RecentApiResponse
import model.ScheduleApiResponse
import model.AnimeItem
import model.FSearchResponse
import model.SearchRequest

interface IKickassAnimeApi {
    suspend fun getFrontPageAnimeList(pageNo: Long): ApiState<RecentApiResponse?>

    suspend fun getFrontPageAnimeListSub(pageNo: Long): ApiState<RecentApiResponse?>

    suspend fun getFrontPageAnimeListDub(pageNo: Long): ApiState<RecentApiResponse?>

    suspend fun getFilters(): ApiState<Filters?>

    suspend fun search(query: SearchRequest): ApiState<FSearchResponse?>

    suspend fun searchHints(query: SearchRequest): ApiState<List<AnimeItem>?>

    suspend fun getEpisode(path: String): ApiState<EpisodeApiResponse?>

    suspend fun getEpisodes(
        path: String,
        language: String,
        page: Int
    ): ApiState<EpisodesResponse?>

    suspend fun getLanguage(path: String): ApiState<BaseApiResponse<String>?>

    suspend fun getSchedule(): ApiState<List<ScheduleApiResponse>>

    suspend fun getPopularAnimes(pageNo: Long): ApiState<AnimeResponse?>

    suspend fun getTrendingAnimes(pageNo: Long): ApiState<AnimeResponse?>

    suspend fun getAllAnimes(pageNo: Long): ApiState<AnimeResponse?>
}