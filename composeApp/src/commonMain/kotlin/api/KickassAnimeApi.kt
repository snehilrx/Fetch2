package api

import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import model.AnimeSearchResponse
import model.BaseApiResponse
import model.EpisodeApiResponse
import model.EpisodesResponse
import model.Filters
import model.RecentApiResponse
import model.SearchItem
import model.SearchRequest

object KickassAnimeApi {
    suspend fun getFrontPageAnimeList(pageNo: Int): RecentApiResponse {
        return get("/api/show/recent?type=all") {
            parameter("page", pageNo)
        }.body<RecentApiResponse>()
    }

    suspend fun getFrontPageAnimeListSub(pageNo: Int): RecentApiResponse {
        return get("/api/show/recent?type=sub") {
            parameter("page", pageNo)
        }.body<RecentApiResponse>()
    }

    suspend fun getFrontPageAnimeListDub(pageNo: Int): RecentApiResponse {
        return get("/api/show/recent?type=dub") {
            parameter("page", pageNo)
        }.body<RecentApiResponse>()
    }

    suspend fun getFilters(): Filters {
        return get("api/show/filters"){}.body<Filters>()
    }

    suspend fun search(query: SearchRequest): AnimeSearchResponse {
        return post("/api/fsearch") {
            setBody(query)
        }.body<AnimeSearchResponse>()
    }

    suspend fun searchHints(query: SearchRequest): List<SearchItem> {
        return post("/api/search") {
            setBody(query)
        }.body<List<SearchItem>>()
    }


    suspend fun getEpisode(path: String): EpisodeApiResponse {
        return get("/api/episode/${path}"){}.body<EpisodeApiResponse>()
    }

    suspend fun getEpisodes(
        path: String,
        language: String,
        page: Int
    ): EpisodesResponse? {
        return get("/api/show/${path}/episodes") {
            parameter("page", page)
            parameter("lang", language)
        }.body<EpisodesResponse>()
    }

    suspend fun getLanguage(path: String): BaseApiResponse<String> {
        return get("/api/show/${path}/language") {}.body<BaseApiResponse<String>>()
    }

    suspend fun get(path: String, block: HttpRequestBuilder.() -> Unit) = Ktor.get(ProxyApi.getReachableProxyFromList() + path) {
        block.invoke(this)
    }

    suspend fun post(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        ProxyApi.getReachableProxyFromList()
        return Ktor.post(ProxyApi.getReachableProxyFromList() + path) {
            block.invoke(this)
        }
    }
}