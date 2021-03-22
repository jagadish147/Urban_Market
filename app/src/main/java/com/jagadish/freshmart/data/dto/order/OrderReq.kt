package com.jagadish.freshmart.data.dto.order

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class OrderReq(
    @Json(name = "cart_id")
    val cart_id: Int = 0,
    @Json(name = "customer_id")
    val customer_id: Int = 0,
    @Json(name = "customer_address_id")
    val customer_address_id: Int = 0,
): Parcelable