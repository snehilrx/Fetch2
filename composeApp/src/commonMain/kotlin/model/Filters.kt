package model

import kotlinx.serialization.Serializable

@Serializable
data class Filters(
    val genres: List<String>?,
    val types: List<String>?,
    val years: List<String>?,
)