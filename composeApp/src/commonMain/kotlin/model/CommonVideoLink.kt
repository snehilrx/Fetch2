package model

interface CommonVideoLink {

    fun getLink(): String
    fun getLinkName(): String
    fun getVideoType(): Int

    companion object {
        const val DASH = 0
        const val HLS = 1
    }
}

