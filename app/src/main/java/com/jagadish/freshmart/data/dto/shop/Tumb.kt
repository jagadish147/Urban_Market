package com.jagadish.freshmart.data.dto.shop

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
class Tumb(
    @Json(name = "url")
    val url: String = "",
): Parcelable