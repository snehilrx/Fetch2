import api.impl.ProxyApi
import com.otaku.fetch2.Anime
import com.otaku.fetch2.AnimeGenre
import com.otaku.fetch2.AnimeLanguage
import com.otaku.fetch2.Episode
import com.otaku.fetch2.VideoHistory
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.EpisodeApiResponse
import model.EpisodesWithPreview
import model.Recent
import model.AnimeItem
import utils.Utils

fun Recent.asAnimeEntity(): Anime {
    return Anime(
        animeSlug = slug ?: "",
        name = if (!this.titleEn.isNullOrEmpty()) {
            this.titleEn
        } else {
            this.title
        },
        image = this.poster?.hq?.removeSuffix("-hq") ?: this.poster?.sm?.removeSuffix("-sm"),
        year = year?.toLong(),
        description = this.synopsis,
        favourite = 0L,
        rating = null,
        status = null,
        type = null
    )
}

fun Recent.asEpisodeEntity(): Episode {
    return Episode(
        episodeSlug = getEpisodeSlug(),
        animeSlug = slug,
        duration = this.duration,
        language = this.language,
        episodeNumber = this.episodeNumber?.toDouble(),
        title = this.episodeTitle,
        createdDate = this.createdAt?.let { Utils.parseDateTime(it).toString() },
        favourite = null,
        link1 = null,
        link2 = null,
        link3 = null,
        link4 = null,
        next = null,
        prev = null,
        thumbnail = null
    )
}

fun EpisodeApiResponse.asEpisodeEntity(e: Episode): Episode {
    val response = this
    return Episode(
        animeSlug = response.showSlug,
        link1 = response.servers.getOrNull(0),
        link2 = response.servers.getOrNull(1),
        next = response.nextEpSlug,
        title = response.title,
        link4 = response.servers.getOrNull(2),
        prev = response.prevEpSlug,
        link3 = response.servers.getOrNull(3),
        language = response.language,
        thumbnail = response.thumbnail?.hq?.removeSuffix("-hq")
            ?: response.thumbnail?.sm?.removeSuffix("-sm"),
        createdDate = e.createdDate,
        duration = e.duration,
        episodeNumber = e.episodeNumber,
        episodeSlug = e.episodeSlug,
        favourite = e.favourite
    )
}

fun EpisodeApiResponse.asAnimeEntity(anime: Anime): Anime {
    val newAnimeData = this
    return Anime(
        name = newAnimeData.title,
        description = newAnimeData.synopsis,
        animeSlug = newAnimeData.showSlug ?: "",
        favourite = anime.favourite,
        rating = anime.rating,
        image = anime.image,
        status = anime.status,
        type = anime.type,
        year = anime.year,
    )
}

fun Episode.asVideoHistory(): VideoHistory {
    return VideoHistory(
        episodeSlug,
        lastPlayed = Clock.System.now().toLocalDateTime(TimeZone.UTC).toString(),
        timestamp = 0
    )
}

fun AnimeItem.asAnimeEntity(): Anime {
    return Anime(
        animeSlug = this.slug ?: "",
        name = if (this.titleEn.isNullOrEmpty()) {
            this.titleText
        } else {
            this.titleEn
        },
        image = this.poster?.hq?.removeSuffix("-hq") ?: this.poster?.sm?.removeSuffix("-sm"),
        year = this.year?.toLong(),
        description = this.synopsis,
        type = this.type,
        rating = this.rating,
        favourite = 0L,
        status = this.status
    )
}

fun AnimeItem.asLanguageEntity(): List<AnimeLanguage> {
    return this.locales.map {
        AnimeLanguage(
            animeSlug = this.slug ?: "",
            language = it
        )
    }
}

fun AnimeItem.asAnimeGenreEntity(): List<AnimeGenre> {
    return this.genres.map {
        AnimeGenre(
            animeSlug = this.slug ?: "",
            genre = it
        )
    }
}

fun EpisodesWithPreview.asEpisodeEntity(
    animeSlug: String,
    language: String,
    prev: String?,
    next: String?
): Episode {
    return Episode(
        episodeNumber = episodeNumber?.toDouble(),
        title = title,
        duration = duration_ms,
        episodeSlug = slug(),
        animeSlug = animeSlug,
        thumbnail = this.thumbnail?.hq?.removeSuffix("-hq")
            ?: this.thumbnail?.sm?.removeSuffix("-sm"),
        language = language,
        prev = prev,
        next = next,
        favourite = null,
        link1 = null,
        link2 = null,
        link3 = null,
        link4 = null,
        createdDate = null
    )
}

fun EpisodesWithPreview.slug(): String {
    return "ep-${episodeNumber?.toInt()}-$slug"
}

fun String.slugToEpisodeLink(animeSlug: String) =
    "${ProxyApi.getCachedEndpoint()}/$animeSlug/$this"