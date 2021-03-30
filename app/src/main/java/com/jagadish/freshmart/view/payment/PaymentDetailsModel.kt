package com.jagadish.freshmart.view.payment

import android.os.Parcelable
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.order.OrderRes
import com.jagadish.freshmart.data.dto.order.PaymentStatusRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentDetailsModel(
    var userName :String = "",
    var userMobile : String = "",
    var orderItemsCount : Int = 0,
    var orderDeliveryAddress : AddAddressReq ,
    var OrderTotalAmount : Double = 0.0,
    var OrderDiscount :  Double = 0.0,
    var OderDeliveryCharge :  Double = 0.0,
    var orderTotalPayable :  Double = 0.0,
    var orderres : OrderRes,
): Parcelable
