package com.jagadish.freshmart.data.dto.cart

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Created by Jagadeesh on 05-03-2021.
 */
@JsonClass(generateAdapter = false)
@Parcelize
data class Cart (
    @Json(name = "success")
    val status: Boolean = false,
    @Json(name = "message")
    val message: String = "",
    @Json(name = "items")
    val products: List<ProductsItem> = ArrayList(),
    @Json(name = "total")
    var order_price: Double = 0.0,
    @Json(name = "delivery_charge")
    val delivery_charge: Double = 0.0,
    @Json(name = "count")
    val count : Int = 0,
    var total_price: Double = order_price,

): Parcelable