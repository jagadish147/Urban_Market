package com.jagadish.freshmart.data.dto.products

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class ProductsItem (
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "image")
    val image: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "price")
    val price : Double = 0.0,
    @Json(name = "discount")
    val discount : Int = 0,
    @Json(name = "quantity")
    var quantity : Int = 0,
    var isAddCart : Boolean = false
): Parcelable