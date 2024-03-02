package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EpisodeApiResponse(
    @SerialName("title") var title: String? = null,
    @SerialName("title_en") var titleEn: String? = null,
    @SerialName("synopsis") var synopsis: String? = null,
    @SerialName("episode_title") var episodeTitle: String? = null,
    @SerialName("episode_string") var episodeString: String? = null,
    @SerialName("language") var language: String? = null,
    @SerialName("thumbnail") var thumbnail: Images? = null,
    @SerialName("poster") var poster: Images? = null,
    @SerialName("banner") var banner: Images? = null,
    @SerialName("broadcast_day") var broadcastDay: String? = null,
    @SerialName("broadcast_time") var broadcastTime: String? = null,
    @SerialName("slug") var slug: String? = null,
    @SerialName("show_slug") var showSlug: String? = null,
    @SerialName("servers") var servers: ArrayList<String> = arrayListOf(),
    @SerialName("next_ep_slug") var nextEpSlug: String? = null,
    @SerialName("prev_ep_slug") var prevEpSlug: String? = null
)
