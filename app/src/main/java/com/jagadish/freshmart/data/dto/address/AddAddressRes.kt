package com.jagadish.freshmart.data.dto.address

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class AddAddressRes (
    @Json(name = "success")
    val success: Boolean = false,
    @Json(name = "status")
    val status : Int = 0,
    @Json(name = "message")
    val message : String = "",
) : Parcelable