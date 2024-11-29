package model

import kotlinx.serialization.SerialName


enum class TimestampType(val type: String) {
    INTRO("Intro"),
    RECAP("Recap"),
    CANON("Canon"),
    MUST_WATCH("Must Watch"),
    BRANDING("Branding"),
    MIXED_INTRO("Mixed Intro"),
    NEW_INTRO("New Intro"),
    FILLER("Filler"),
    TRANSITION("Transition"),
    CREDITS("Credits"),
    MIXED_CREDITS("Mixed Credits"),
    NEW_CREDITS("New Credits"),
    PREVIEW("Preview"),
    TITLE_CARD("Title Card"),
    UNKNOWN("Unknown")
}

data class Type(
    val name: String
)

data class Timestamps(
    val type: Type,
    @SerialName("at") var at: Float? = null
)

data class Episodes(
    val number: String?,
    @SerialName("timestamps") var timestamps: ArrayList<Timestamps> = arrayListOf()
)

data class Data(
    @SerialName("findEpisodesByShowId") var episodes: ArrayList<Episodes> = arrayListOf()
)

data class Timeline(
    @SerialName("data") var data: Data? = Data()
)