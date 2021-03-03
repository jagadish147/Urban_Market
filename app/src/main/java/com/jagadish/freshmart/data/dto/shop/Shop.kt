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
data class Shop(
    @Json(name = "status")
    val status: Boolean = false,
    @Json(name = "message")
    val message: String = "",
    @Json(name = "categories")
    val shopList: List<ShopItem>,
    @Json(name = "banners")
    val banners: List<ShopItem>,
    ): Parcelable
