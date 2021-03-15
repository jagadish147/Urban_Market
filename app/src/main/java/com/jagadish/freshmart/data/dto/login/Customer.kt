package com.jagadish.freshmart.data.dto.login

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class Customer (
    @Json(name = "name")
    val name: String = "",
    @Json(name = "email")
    val email: String = "",
    @Json(name = "id")
    val id : Int = 0,
    @Json(name = "address")
    val address: List<Address> = ArrayList(),
    @Json(name = "phone")
    val phone : String = "",
): Parcelable