package com.jagadish.freshmart.data.dto.address

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class AddAddressReq (
    @Json(name = "id")
    var id: Int = 0,
    @Json(name = "phone_number")
    val phone_number : String = "",
    @Json(name = "address_line1")
    val address_line1 : String = "",
    @Json(name = "address_line2")
    val address_line2 : String = "",
    @Json(name = "city")
    val city : String = "",
    @Json(name = "state")
    val state : String = "",
    @Json(name = "zip")
    val zip : String = "",
    @Json(name = "default")
    var defaultAddress : Boolean = false,
    @Json(name = "address_type")
    val address_type : String = "defalut",// defalut, reside,home

) : Parcelable