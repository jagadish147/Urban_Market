package com.jagadish.freshmart.data.dto.order

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class OrdersRes(
    @Json(name = "success")
    val success: Boolean = false,
    @Json(name = "status")
    val status: Int = 0,
    @Json(name = "message")
    val message : String = "",
    @Json(name = "orders")
    val orders : List<OrderItems> = ArrayList(),
): Parcelable
