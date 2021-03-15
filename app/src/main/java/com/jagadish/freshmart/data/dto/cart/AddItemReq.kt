package com.jagadish.freshmart.data.dto.cart

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class AddItemReq(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "product_id")
    val product_id : Int = 0,
) : Parcelable