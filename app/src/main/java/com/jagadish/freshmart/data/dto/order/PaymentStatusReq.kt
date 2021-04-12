package com.jagadish.freshmart.data.dto.order

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class PaymentStatusReq(
    @Json(name = "order_number")
    val order_number: String = "",
    @Json(name = "mode")
    val mode: String = "",
    @Json(name = "transaction_id")
    val transaction_id: String = "",
    @Json(name = "status")
    var status : String = "",
): Parcelable
