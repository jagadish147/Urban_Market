package com.jagadish.freshmart.data.dto.deliver.orders

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class UpdateOrderStatus(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "order_number")
    val order_number: String = "",
    @Json(name = "status")
    val status: String = "",
) : Parcelable
