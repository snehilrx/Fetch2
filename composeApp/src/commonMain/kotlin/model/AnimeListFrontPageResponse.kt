package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RecentApiResponse : BaseApiResponse<Recent>()

@Serializable
data class Recent(
    @SerialName("language") var language: String? = null,
    var duration: Long = 0,
    @SerialName("created_at") var createdAt: String? = null,
    @SerialName("locales") var locales: ArrayList<String> = arrayListOf(),
    @SerialName("title") var title: String? = null,
    @SerialName("title_en") var titleEn: String? = null,
    @SerialName("synopsis") var synopsis: String? = null,
    @SerialName("episode_title") var episodeTitle: String? = null,
    @SerialName("episode_number") var episodeNumber: Float? = null,
    @SerialName("episode_string") var episodeString: String? = null,
    @SerialName("poster") var poster: Images? = null,
    @SerialName("type") var type: String? = null,
    @SerialName("year") var year: Int? = null,
    @SerialName("rating") var rating: String? = null,
    @SerialName("slug") var slug: String? = null,
    @SerialName("watch_uri") private var watchUri: String? = null
) {
    fun getEpisodeSlug() = watchUri?.split("/")?.getOrNull(2) ?: ""
}

@Serializable
data class Images(
    @SerialName("sm") var sm: String?,
    @SerialName("hq") var hq: String?
)

@Serializable
open class BaseApiResponse<T>(
    @SerialName("hadNext") var hasNext: Boolean = false,
    var result: ArrayList<T> = arrayListOf()
)