package com.jagadish.freshmart.data.dto.order

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class OrderItems(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "order_date")
    val order_date: String = "",
    @Json(name = "delivery_date")
    val delivery_date : String = "",
    @Json(name = "number")
    val number : String = "",
    @Json(name = "price")
    val price : String = "",
    @Json(name = "status")
    val status : String = "",
    @Json(name = "delivery_charge")
    val delivery_charge : Double = 0.0,
    @Json(name = "items_count")
    val item_count : Int = 0,
    @Json(name = "delivery_address")
    val delivery_address : String = "",
    @Json(name = "items")
    val items : MutableList<ProductsItem> = ArrayList(),

    ): Parcelable
