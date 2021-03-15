package com.jagadish.freshmart.data.dto.login

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class CustomersRequest (
    @Json(name = "phone_number")
    val phone_number: String = "",
    @Json(name = "email")
    val email: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "cart_id")
    var cart_id: Int = 0,
): Parcelable