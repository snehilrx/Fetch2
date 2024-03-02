package api

import Constants
import com.fleeksoft.ksoup.Ksoup
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

object ProxyApi {
    suspend fun getReachableProxyFromList() : String {
        val response = Ktor.get(Constants.KICKASS_PROXY_LIST).body<String>()
        return Ksoup.parse(response).selectFirst("body > div.container > div:nth-child(2)")
            ?.childElementsList()
            ?.map { it.text() }
            ?.first {
                isProxyWorking(it)
            } ?: DEFAULT
    }

    private suspend fun isProxyWorking(url: String) : Boolean {
        return try {
            Ktor.get(url){}.status == HttpStatusCode.OK
        } catch (e: Exception) {
            false
        }
    }

    private const val DEFAULT = "https://kickassanimes.io/"
}