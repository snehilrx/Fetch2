package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class AnimeSearchResponse(
    val maxPage: Int
) : BaseApiResponse<SearchItem>()

@Serializable
data class SearchItem(
    @SerialName("genres") var genres: ArrayList<String> = arrayListOf(),
    @SerialName("locales") var locales: ArrayList<String> = arrayListOf(),
    @SerialName("episode_count") var episodeCount: Int? = null,
    @SerialName("slug") var slug: String? = null,
    @SerialName("status") var status: String? = null,
    @SerialName("synopsis") var synopsis: String? = null,
    @SerialName("title") var titleText: String? = null,
    @SerialName("title_en") var titleEn: String? = null,
    @SerialName("type") var type: String? = null,
    @SerialName("year") var year: Int? = null,
    @SerialName("poster") var poster: Images? = null,
    @SerialName("episode_duration") var episodeDuration: Int? = null,
    @SerialName("watch_uri") var watchUri: String? = null,
    @SerialName("episode_number") var episodeNumber: Int? = null,
    @SerialName("episode_string") var episodeString: String? = null,
    @SerialName("rating") var rating: String? = null)