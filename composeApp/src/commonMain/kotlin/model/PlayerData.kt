package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerData(
    @SerialName("tracks") var tracks: ArrayList<Tracks> = arrayListOf(),
    @SerialName("key") var key: String? = null,
    @SerialName("image") var image: String? = null,
    @SerialName("audio_locale") var audioLocale: String? = null,
    @SerialName("file") var file: String? = null,
    @SerialName("type") var type: String? = null,
    @SerialName("setupConfig") var setupConfig: Setup? = Setup(),
)

@Serializable
data class Setup(
    @SerialName("tracks") var tracks: ArrayList<Tracks> = arrayListOf(),
    @SerialName("key") var key: String? = null,
    @SerialName("image") var image: String? = null,
    @SerialName("audio_locale") var audioLocale: String? = null,
    @SerialName("file") var file: String? = null,
    @SerialName("type") var type: String? = null,
)

@Serializable
data class Tracks(
    @SerialName("kind") var kind: String? = null,
    @SerialName("file") var file: String? = null,
    @SerialName("label") var label: String? = null,
    @SerialName("default") var default: Boolean? = null
)