package com.jagadish.freshmart.data.dto.deliver.orders

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class UpdateOrderStatusRes(
    @Json(name = "success")
    val success: Boolean = false,
    @Json(name = "message")
    val message: String = "",
    @Json(name = "status")
    val status: Int = 0,
) : Parcelable
