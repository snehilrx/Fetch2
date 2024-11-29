package model

import api.impl.ProxyApi
import com.otaku.fetch2.Anime


class AnimeTile(
    val animeEntity: Anime,
    override val imageUrl: String = "${ProxyApi.getCachedEndpoint()}/image/poster/${animeEntity.image}",
    override val tags: List<String> = listOfNotNull(
        animeEntity.year.toString(),
        animeEntity.type,
        animeEntity.rating
    ),
    val pageNo: Long?,
    override val title: String = animeEntity.name ?: ""
) : ITileData {
    override fun areItemsTheSame(newItem: ITileData): Boolean {
        return newItem is AnimeTile && newItem.animeEntity.animeSlug == this.animeEntity.animeSlug
    }

    override fun areContentsTheSame(newItem: ITileData): Boolean {
        return newItem is AnimeTile && this.animeEntity == newItem.animeEntity
    }

}
