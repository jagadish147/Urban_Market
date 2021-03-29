package com.jagadish.freshmart.data.remote

import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.address.AddAddressRes
import com.jagadish.freshmart.data.dto.address.AddressRes
import com.jagadish.freshmart.data.dto.address.GetAddressReq
import com.jagadish.freshmart.data.dto.cart.*
import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatus
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatusRes
import com.jagadish.freshmart.data.dto.login.CustomersRequest
import com.jagadish.freshmart.data.dto.login.CustomersRes
import com.jagadish.freshmart.data.dto.login.RequestOtpReq
import com.jagadish.freshmart.data.dto.login.RequestOtpRes
import com.jagadish.freshmart.data.dto.order.*
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.shop.Shop

/**
 * Created by Jagadeesh on 01-03-2021.
 */
internal interface RemoteDataSource {
    suspend fun requestCreateCart(fcm_token: CreateCareReq) : Resource<CreateCartRes>

    suspend fun requestRecipes(pinCode:String): Resource<Shop>

    suspend fun requestProducts(categoryId: Int): Resource<Products>

    suspend fun requestCart(cartId: Int): Resource<Cart>

    suspend fun requestAddItem(addItemReq: AddItemReq): Resource<AddItemRes>

    suspend fun requestRemoveItem(addItemReq: AddItemReq): Resource<AddItemRes>

    suspend fun requestOTP(addItemReq: RequestOtpReq): Resource<RequestOtpRes>

    suspend fun createCustomers(customersRes: CustomersRequest): Resource<CustomersRes>

    suspend fun requestCustomerLogin(customersRes: RequestOtpReq): Resource<CustomersRes>

    suspend fun requestAddAddress(customerId: Int,addAddressReq: AddAddressReq): Resource<AddAddressRes>

    suspend fun requestRemoveAddress(customerId: Int,addAddressReq: AddAddressReq): Resource<AddAddressRes>

    suspend fun requestUpdateAddress(customerId: Int,addAddressReq: AddAddressReq): Resource<AddAddressRes>

    suspend fun requestAddress(customerId: Int,phoneNumber : String): Resource<AddressRes>

    suspend fun requestOrderId(orderReq : OrderReq): Resource<OrderRes>

    suspend fun requestPaymentStatus(paymentStausReq : PaymentStatusReq): Resource<PaymentStatusRes>

    suspend fun requestDeliveryBoyLogin(addItemReq: RequestOtpReq): Resource<RequestOtpRes>

    suspend fun requestDeliveryBoyScheduleOrders(deliveryBoyId: Int): Resource<DeliveryBoyOrders>

    suspend fun requestDeliveryBoyAllOrders(deliveryBoyId: Int, all : Boolean): Resource<DeliveryBoyOrders>

    suspend fun requestCustomerOrders(phoneNumber: String): Resource<OrdersRes>

    suspend fun requestUpdateOrderStatus(updateOrderStatus : UpdateOrderStatus): Resource<UpdateOrderStatusRes>
}