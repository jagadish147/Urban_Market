package com.jagadish.freshmart.data.dto.shop

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Created by Jagadeesh on 01-03-2021.
 */
@JsonClass(generateAdapter = false)
@Parcelize
data class ShopItem(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "image")
    val image: String = "",
    @Json(name = "name")
    val name: String = "",
):Parcelable
