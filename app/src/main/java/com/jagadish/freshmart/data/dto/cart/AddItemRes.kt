package com.jagadish.freshmart.data.dto.cart

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class AddItemRes(
    @Json(name = "success")
    val success: Boolean = false,
    @Json(name = "message")
    val message : String = "",
    @Json(name = "count")
    val count : Int = 0,
    @Json(name = "total_price")
    val total_price : Double = 0.0,
    @Json(name = "items")
    val items : MutableList<ProductsItem>  = ArrayList(),
    @Json(name = "delivery_charge")
    val delivery_charge : Double = 0.0,
): Parcelable