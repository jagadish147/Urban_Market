package com.jagadish.freshmart.data.dto.login

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class Address (

    @Json(name = "id")
    val id : Int = 0,
    @Json(name = "address_line1")
    val address_line1: String = "",
    @Json(name = "address_line2")
    val address_line2: String = "",
    @Json(name = "city")
    val city: String = "",
    @Json(name = "state")
    val state : String = "",
    @Json(name="zip")
    val zip : Int = 0,
    @Json(name = "address_type")
    val address_type : String = "",
    @Json(name = "phone")
    val phone : String = "",
    @Json(name = "customer_id")
    val customer_id : Int =0,
): Parcelable