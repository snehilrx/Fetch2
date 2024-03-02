package model

import kotlinx.serialization.SerialName

data class Maverickki(
    @SerialName("videoId") var videoId: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("thumbnail") var thumbnail: String? = null,
    @SerialName("timelineThumbnail") var timelineThumbnail: String? = null,
    @SerialName("hls") var hls: String? = null,
    @SerialName("renditionInProgress") var renditionInProgress: Boolean? = null,
    @SerialName("subtitles") var subtitles: ArrayList<Subtitles> = arrayListOf()
) {
    companion object {
        internal const val BASE_URL = "https://maverickki.com"
    }

    @Suppress("unused")
    fun link(): String? {
        return if (hls != null) BASE_URL + hls else null
    }
}

data class Subtitles(
    @SerialName("name") var name: String? = null,
    @SerialName("src") var src: String? = null
) : CommonSubtitle {
    override fun getLanguage(): String {
        return when (name) {
            "Traditional Chinese",
            "Simplified Chinese" -> "chinese"

            "Indonesian" -> "in"
            "Vietnamese" -> "vi"
            else -> name ?: ""
        }
    }

    override fun getLink(): String {
        return if (src != null) Maverickki.BASE_URL + src else ""
    }

    override fun getFormat(): String = MimeTypes.TEXT_VTT.code
}