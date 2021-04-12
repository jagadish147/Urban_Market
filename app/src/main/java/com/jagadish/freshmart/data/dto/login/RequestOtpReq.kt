package com.jagadish.freshmart.data.dto.login

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class RequestOtpReq (
    @Json(name = "phone_number")
    val phone_number: String = "",
    @Json(name = "password")
    val password : String = "",
    @Json(name = "login")
    val login : String = "",
    @Json(name = "auth_token")
    val auth_token : String ="",
    @Json(name = "cart_id")
    val car_id : Int = 0,
    ): Parcelable