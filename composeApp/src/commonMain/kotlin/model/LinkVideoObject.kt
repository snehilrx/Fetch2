package model

class LinkVideoObject(
    private val url: String,
    private val mediaType: Int
) : CommonVideoLink {
    override fun getLink(): String {
        return url
    }

    override fun getLinkName(): String {
        return try {
            url.replace("https://", "").substringBefore('/')
        } catch (e: NullPointerException) {
            "Kick Server"
        }
    }

    override fun getVideoType(): Int {
        return mediaType
    }

}