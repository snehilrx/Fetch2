package api.impl

import api.IKickassAnimeApi
import api.IProxyApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.error
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
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

internal class KickassAnimeApi(
    private val httpClient: HttpClient,
    private val proxyApi: IProxyApi,
) : IKickassAnimeApi {

    override suspend fun getFrontPageAnimeList(pageNo: Long): ApiState<RecentApiResponse?> {
        return get("/api/show/recent?type=all") {
            parameter("page", pageNo)
        }
    }

    override suspend fun getFrontPageAnimeListSub(pageNo: Long): ApiState<RecentApiResponse?> {
        return get("/api/show/recent?type=sub") {
            parameter("page", pageNo)
        }
    }

    override suspend fun getFrontPageAnimeListDub(pageNo: Long): ApiState<RecentApiResponse?> {
        return get("/api/show/recent?type=dub") {
            parameter("page", pageNo)
        }
    }

    override suspend fun getFilters(): ApiState<Filters?> {
        return get("api/show/filters"){}
    }

    override suspend fun search(query: SearchRequest): ApiState<FSearchResponse?> {
        return post("/api/fsearch") {
            setBody(query)
        }
    }

    override suspend fun searchHints(query: SearchRequest): ApiState<List<AnimeItem>?> {
        return post("/api/search") {
            setBody(query)
        }
    }


    override suspend fun getEpisode(path: String): ApiState<EpisodeApiResponse?> {
        return get("/api/episode/${path}"){}
    }

    override suspend fun getEpisodes(
        path: String,
        language: String,
        page: Int
    ): ApiState<EpisodesResponse?> {
        return get("/api/show/${path}/episodes") {
            parameter("page", page)
            parameter("lang", language)
        }
    }

    override suspend fun getLanguage(path: String): ApiState<BaseApiResponse<String>?> {
        return get("/api/show/${path}/language") {}
    }

    override suspend fun getSchedule(): ApiState<List<ScheduleApiResponse>> {
        return get("/api/schedule") {}
    }

    override suspend fun getPopularAnimes(pageNo: Long): ApiState<AnimeResponse?> {
        return get("/api/show/popular") {
            parameter("page", pageNo)
        }
    }

    override suspend fun getTrendingAnimes(pageNo: Long): ApiState<AnimeResponse?> {
        return get<AnimeResponse?>("/api/show/trending") {
            parameter("page", pageNo)
        }
    }

    override suspend fun getAllAnimes(pageNo: Long): ApiState<AnimeResponse?> {
        val fSearchResponse = get<FSearchResponse?>("/api/anime") {
            parameter("page", pageNo)
        }
        return when (fSearchResponse) {
            is ApiState.COMPLETED -> {
                ApiState.COMPLETED(
                    AnimeResponse(pageNo.toInt()).apply {
                        result = fSearchResponse.value?.result
                    }
                )
            }

            is ApiState.ERROR -> {
                ApiState.ERROR(fSearchResponse.message)
            }

            else -> {
                ApiState.ERROR("something went wrong")
            }
        }
    }

    private suspend inline fun <reified T> get(
        path: String,
        block: HttpRequestBuilder.() -> Unit): ApiState<T> {
        return callApi<T>(path) { urlString, blockCall ->
            httpClient.get(urlString) {
                block()
                blockCall()
            }
        }
    }

    private suspend inline fun <reified T> callApi(
        path: String,
        method: (urlString: String, block: HttpRequestBuilder.() -> Unit) -> HttpResponse,
    ): ApiState<T> {

        supervisorScope {

        }
        return try {
            val response = method(proxyApi.getReachableProxyFromList() + path) {
                headers {
                    append(
                        "user-agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36"
                    )
                    append(
                        "Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
                    )
                    append(
                        "Sec-Ch-Ua",
                        "\"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\""
                    )
                }
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    ApiState.COMPLETED(response.body<T>())
                }

                HttpStatusCode.BadRequest -> ApiState.ERROR("Bad Request: The server could not understand the request due to invalid syntax.")
                HttpStatusCode.Unauthorized -> ApiState.ERROR("Unauthorized: Access is denied due to invalid credentials.")
                HttpStatusCode.Forbidden -> ApiState.ERROR("Forbidden: You do not have the necessary permissions to access this resource.")
                HttpStatusCode.NotFound -> ApiState.ERROR("Not Found: The requested resource could not be found on the server.")
                HttpStatusCode.InternalServerError -> ApiState.ERROR("Internal Server Error: The server encountered an internal error. Please try again later.")
                HttpStatusCode.ServiceUnavailable -> ApiState.ERROR("Service Unavailable: The server is currently unable to handle the request. Please try again later.")
                else -> ApiState.ERROR("Unexpected status code: ${response.status}")
            }
        } catch (e: UnresolvedAddressException) {
            ApiState.ERROR("The server address could not be resolved. Please check the URL and try again.")
        } catch (e: ClientRequestException) {
            ApiState.ERROR("There was an error with the client request. Please check the request parameters and try again. Error: ${e.message}")
        } catch (e: ServerResponseException) {
            ApiState.ERROR("The server responded with an error. Please try again later. Error: ${e.message}")
        } catch (e: ResponseException) {
            ApiState.ERROR("There was an error with the server response. Please try again later. Error: ${e.message}")
        } catch (e: Exception) {
            ApiState.ERROR("An unexpected error occurred. Please try again. Error: ${e.message}")
        }
    }

    private suspend inline fun <reified T> post(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): ApiState<T> {
        return callApi<T>(path) { urlString, blockCall ->
            httpClient.post(urlString) {
                block()
                blockCall()
            }
        }
    }
}