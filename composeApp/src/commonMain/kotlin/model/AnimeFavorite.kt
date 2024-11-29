package model
import api.impl.ProxyApi

data class AnimeFavorite(
    override val title: String,
    val animeSlug: String,
    val image: String
) : ITileData {
    override fun areItemsTheSame(newItem: ITileData): Boolean {
        return newItem is AnimeFavorite && animeSlug == newItem.animeSlug
    }

    override fun areContentsTheSame(newItem: ITileData): Boolean {
        return newItem is AnimeFavorite && this == newItem
    }


    override val imageUrl: String
        get() = "${ProxyApi.getCachedEndpoint()}/image/poster/$image"

    override val tags: List<String>
        get() = emptyList()
}