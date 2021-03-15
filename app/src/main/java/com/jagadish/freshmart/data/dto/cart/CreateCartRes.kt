package com.jagadish.freshmart.data.dto.cart

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.shop.Image
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class CreateCartRes(@Json(name = "id")
                    val id: Int = 0,
                    @Json(name = "cart_id")
                    val cart_id: String,
                    @Json(name = "success")
                    val success: Boolean = false,
                    @Json(name = "message")
                    val message : String = "",
                    @Json(name = "items")
                    val items : Int = 0,
): Parcelable