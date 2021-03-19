package com.jagadish.freshmart.data.dto.products

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class Products (
    @Json(name = "status")
    val status: Boolean = false,
    @Json(name = "message")
    val message: String = "",
    @Json(name = "products")
    val products: List<ProductsItem> = ArrayList(),
    @Json(name = "count")
    var count : Int = 0,
    @Json(name = "total")
    var total: Double = 0.0,
): Parcelable