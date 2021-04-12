package com.jagadish.freshmart.data.dto.deliver.orders

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.login.Customer
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class ScheduleOrders(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "number")
    val number: String = "",
    @Json(name = "status")
    val status: String = "",
    @Json(name = "order_type")
    val order_type : String = "",
    @Json(name = "items")
    val items : List<ProductsItem> = ArrayList(),
    @Json(name = "address")
    val address : AddAddressReq ,
    @Json(name = "customer")
    val customer : Customer,
    @Json(name = "total")
    val total : Double = 0.0,
): Parcelable
