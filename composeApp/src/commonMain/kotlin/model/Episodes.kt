package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EpisodesWithPreview(
    @SerialName("episode_number") var episodeNumber: Float? = null,
    @SerialName("thumbnail") var thumbnail: Images? = null,
    @SerialName("slug") val slug: String? = null,
    var title: String? = null,
    var duration_ms: Long? = null
)

@Serializable
data class Page(
    val number: Int,
)

@Serializable
data class EpisodesResponse(
    val pages: List<Page>?,
    @SerialName("result") var result: ArrayList<EpisodesWithPreview>? = arrayListOf()
)