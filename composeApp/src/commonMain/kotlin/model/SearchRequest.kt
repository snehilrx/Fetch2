package model

import kotlinx.serialization.Serializable


@Serializable
data class SearchRequest(
    val query: String,
    val page: Int? = null,
    // base64 json string
    val filters: String? = null
)