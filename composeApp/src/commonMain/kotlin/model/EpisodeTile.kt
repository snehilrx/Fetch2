package model

import api.impl.ProxyApi


data class EpisodeTile(
    override val title: String?,
    val animeSlug: String,
    val episodeSlug: String?,
    val image: String?,
    val language: String?,
    val episodeNumber: Double?,
    val pageNo: Long?
) : ITileData {
    override fun areItemsTheSame(newItem: ITileData): Boolean {
        return newItem is EpisodeTile && episodeSlug == newItem.episodeSlug
    }

    override fun areContentsTheSame(newItem: ITileData): Boolean {
        return newItem is EpisodeTile && this == newItem
    }

    override val imageUrl: String
        get() = "${ProxyApi.getCachedEndpoint()}/image/poster/$image"
    override val tags: List<String>
        get() = listOfNotNull(language, "EP $episodeNumber")
}