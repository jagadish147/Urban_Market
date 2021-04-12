package com.jagadish.freshmart.data.dto.login

import android.os.Parcelable
import com.google.firebase.auth.PhoneAuthProvider
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class RequestOtpRes (
    @Json(name = "success")
    val success: Boolean = false,
    @Json(name = "status")
    val status: Int = 0,
    @Json(name = "message")
    val message: String = "",
    @Json(name = "customer_otp")
    val customer_otp: String = "",
    var mobileNumber: String = "",
    @Json(name="driver_id")
    val driver_id : Int = 0,
    @Json(name = "name")
    val name : String ="",
    @Json(name = "email")
    val email : String = "",
    @Json(name = "phone_number")
    val phone_number : String = "",
    var fcmOtp : String = "",
    var fcmResendToken : PhoneAuthProvider.ForceResendingToken = PhoneAuthProvider.ForceResendingToken.zza(),
    ): Parcelable