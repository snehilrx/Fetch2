package api.impl

import CacheKeys
import Constants
import api.IProxyApi
import com.fleeksoft.ksoup.Ksoup
import com.otaku.fetch2.Database
import getValue
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

private const val DEFAULT = "https://kickassanimes.io/"

internal class ProxyApi(
    private val httpClient: HttpClient,
    private val database: Database
    ) : IProxyApi {


    override suspend fun getReachableProxyFromList() : String {
        if (endpoint == null) {
            val cachedProxy =
                database.cacheQueries.getValue(CacheKeys.REACHABLE_PROXY).executeAsOneOrNull()
            val link = cachedProxy ?: Ksoup.parse(httpClient.get(Constants.KICKASS_PROXY_LIST).body<String>()).selectFirst("body > div.container > div")
                ?.childElementsList()
                ?.map {
                    it.attr("href")
                }?.firstOrNull {
                    isProxyWorking(it)
                } ?: DEFAULT
            endpoint = link
            if (cachedProxy == null) {
                database.cacheQueries.insert(CacheKeys.REACHABLE_PROXY of link)
            }
        }
        return endpoint ?: DEFAULT
    }

    private suspend fun isProxyWorking(url: String) : Boolean {
        return try {
            httpClient.get(url){}.status == HttpStatusCode.OK
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private var endpoint: String? = null

        fun getCachedEndpoint() = endpoint
    }
}

