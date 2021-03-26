package com.jagadish.freshmart.data.dto.products

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.shop.Image
import com.jagadish.freshmart.data.dto.shop.Tumb
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class ProductsItem (
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "image")
    val image: Image = Image("", Tumb("")),
    @Json(name = "name")
    val name: String = "",
    @Json(name = "price")
    val price : Double = 0.00,
    @Json(name = "discount_price")
    val discount_price : Double = 0.00,
    @Json(name = "quantity")
    var quantity : Int = 0,
    @Json(name ="unit")
    val unit : String = "",
    @Json(name = "description")
    val description:String ="",
    var isAddCart : Boolean = false,
    @Json(name = "image_url")
    val image_url : String = "",
): Parcelable