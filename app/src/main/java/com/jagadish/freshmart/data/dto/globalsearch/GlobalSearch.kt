package com.jagadish.freshmart.data.dto.globalsearch

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class GlobalSearch (
    @Json(name = "status")
    val status: Int = 0,
    @Json(name = "message")
    val message: String = "",
    @Json(name = "products")
    val products: MutableList<ProductsItem> = ArrayList(),
    @Json(name = "categories")
    val categories: MutableList<ShopItem> = ArrayList(),
    @Json(name = "success")
    var success : Boolean = false,
): Parcelable