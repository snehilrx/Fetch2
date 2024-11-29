package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleApiResponse(
    @SerialName("title"    ) var title   : String? = null,
    @SerialName("title_en" ) var titleEn : String? = null,
    @SerialName("slug"     ) var slug    : String? = null,
    @SerialName("poster"   ) var poster  : Images? = null,
    @SerialName("ts") var releaseTime: Long?
)