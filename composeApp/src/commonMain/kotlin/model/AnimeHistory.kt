package model

import api.impl.ProxyApi
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import utils.Utils


data class AnimeHistory(
    override val title: String,
    val animeSlug: String,
    val episodeSlug: String,
    val image: String,
    val episodeNumber: String?,
    val lastPlayed: LocalDateTime?
) : ITileData {
    override fun areItemsTheSame(newItem: ITileData): Boolean {
        return newItem is AnimeHistory && episodeSlug == newItem.episodeSlug
    }

    override fun areContentsTheSame(newItem: ITileData): Boolean {
        return newItem is AnimeHistory && this == newItem
    }

    override val imageUrl: String
        get() = "${ProxyApi.getCachedEndpoint()}/image/poster/$image"

    override val tags: List<String>
        get() = listOf(
            "EP ${episodeNumber.toString()}", Utils.getRelativeTimeSpanString(lastPlayed)
        )
}
