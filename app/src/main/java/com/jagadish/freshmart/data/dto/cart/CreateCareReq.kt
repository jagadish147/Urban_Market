package com.jagadish.freshmart.data.dto.cart

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class CreateCareReq (@Json(name = "fcm_token")
                     val fcm_token: String = "",): Parcelable