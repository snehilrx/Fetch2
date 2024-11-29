import app.cash.sqldelight.Query
import com.otaku.fetch2.Cache
import com.otaku.fetch2.CacheQueries

object Constants {
    const val DB_FILE = "fetch3.db"
    const val NETWORK_PAGE_SIZE = 36
    const val KICKASS_PROXY_LIST = "https://kickassanimes.info"
}

enum class CacheKeys {
    REACHABLE_PROXY;

    infix fun of(value: String) = Cache(this.ordinal.toLong(), value)

}

fun CacheQueries.getValue(key: CacheKeys) = getValue(key.ordinal.toLong())