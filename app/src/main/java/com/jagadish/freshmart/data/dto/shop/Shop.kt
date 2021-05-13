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
    @Json(name = "success")
    val success: Boolean = false,
    @Json(name = "message")
    val message: String = "",
    @Json(name ="status")
    val status : Int = 0,
    @Json(name = "categories")
    val shopList: List<ShopItem> = ArrayList(),
    @Json(name = "banners")
    val banners: List<ShopItem> = ArrayList(),
    @Json (name= "delivery_message")
    val delivery_message: String = "",
    ): Parcelable
