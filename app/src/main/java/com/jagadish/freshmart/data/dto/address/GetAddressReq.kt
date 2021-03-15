package com.jagadish.freshmart.data.dto.address

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class GetAddressReq (
    @Json(name = "customer_id")
    val customer_id : Int = 0,
    @Json(name = "phone_number")
    val phone_number : String = "",
) : Parcelable