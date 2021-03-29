package com.jagadish.freshmart.data.dto.order

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
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
    @Json(name = "item_count")
    val item_count : Int = 0,
    @Json(name = "status")
    val status : String = "",

): Parcelable
