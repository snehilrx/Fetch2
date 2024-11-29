package utils

import asAnimeEntity
import asEpisodeEntity
import com.otaku.fetch2.Database
import com.otaku.fetch2.EpisodePage
import com.otaku.fetch2.Recent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import model.EpisodesResponse
import slug
import kotlin.jvm.JvmStatic

object Utils {


    fun parseDateTime(dateTime: String): LocalDateTime {
        return try {
            return LocalDateTime.parse(dateTime, LocalDateTime.Formats.ISO)
        } catch (e: Exception) {
//            Log.e(TAG, "parseDateTime: ", e)
            Clock.System.now().toLocalDateTime(TimeZone.UTC)
        }
    }

    @JvmStatic
    suspend fun saveRecent(response: List<model.Recent>, database: Database, pageNo: Long) {
        val data = response.map {
            val anime = it.asAnimeEntity()
            val episode = it.asEpisodeEntity()
            return@map Triple(anime, episode, Recent(
                animeSlug = anime.animeSlug,
                episodeSlug = episode.episodeSlug,
                pageNo = pageNo
            ))
        }
        withContext(Dispatchers.IO) {
            database.transaction {
                data.forEach { (anime, episode, recent) ->
                    database.animeQueries.insert(anime)
                    database.episodeQueries.insert(episode)
                    database.recentQueries.insert(recent)
                }
            }
        }
    }



    fun <T> List<T>.binarySearchGreater(
        fromIndex: Int = 0,
        toIndex: Int = size,
        comparison: (T) -> Int
    ): T? {

        var low = fromIndex
        var high = toIndex - 1
        var ans = -1


        while (low <= high) {
            val mid = (low + high).ushr(1) // safe from overflows
            val midVal = get(mid)
            val cmp = comparison(midVal)

            if (cmp <= 0) {
                low = mid + 1
            } else {
                high = mid - 1
                ans = mid
            }
        }
        return if (ans in fromIndex..toIndex) {
            this[ans]
        } else {
            null
        }
    }

    suspend fun saveEpisodePage(
        animeSlug: String,
        language: String,
        response: EpisodesResponse,
        database: Database,
        pageNo: Int
    ) {
        val result = response.result ?: return
        val episodeEntity = List(result.size) { index ->
            result[index].asEpisodeEntity(
                animeSlug,
                language,
                result.getOrNull(index - 1)?.slug(),
                result.getOrNull(index + 1)?.slug()
            )
        }
        val pages = result.map {
            EpisodePage(it.slug(), pageNo.toLong())
        }
        database.transaction {
            episodeEntity.forEach {
                database.episodeQueries.insert(it)
            }
            pages.forEach {
                database.episodeQueries.insertOrReplace(it)
            }
        }
    }

    fun getRelativeTimeSpanString(timestampVal: LocalDateTime?): String {
        val timestamp = timestampVal ?: return "0 seconds"
        val currentTime = Clock.System.now()
        val duration = currentTime.minus(timestamp.toInstant(UtcOffset.ZERO))

        val days = duration.inWholeDays
        val hours = duration.inWholeHours - days * 24
        val minutes = duration.inWholeMinutes - days * 24 * 60 - hours * 60
        val seconds = duration.inWholeSeconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60

        val stringBuilder = StringBuilder()

        if (days > 0) stringBuilder.append("$days days, ")
        if (hours > 0) stringBuilder.append("$hours hours, ")
        if (minutes > 0) stringBuilder.append("$minutes minutes, ")
        stringBuilder.append("$seconds seconds")

        return stringBuilder.toString()
    }

    fun isURL(input: String): Boolean {
        val urlRegex = """^((http|https|ftp)://)?([a-zA-Z0-9]+(\\.[a-zA-Z]{2,}){1,})(:[0-9]+)?(//.*)?$""".toRegex()
        return urlRegex.matches(input)
    }
}