package com.jagadish.freshmart.data

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
import kotlinx.coroutines.flow.Flow

/**
 * Created by Jagadeesh on 01-03-2021.
 */
interface DataRepositorySource {
    suspend fun requestCreateCart(fcm_token : CreateCareReq): Flow<Resource<CreateCartRes>>

    suspend fun requestRecipes(pinCode:String): Flow<Resource<Shop>>

    suspend fun requestProducts(categoryId: Int): Flow<Resource<Products>>

    suspend fun requestCart(cartId: Int): Flow<Resource<Cart>>

    suspend fun requestAddItem(addItemReq: AddItemReq):Flow<Resource<AddItemRes>>

    suspend fun requestRemoveItem(addItemReq: AddItemReq):Flow<Resource<AddItemRes>>

    suspend fun requestOTP(addItemReq: RequestOtpReq): Flow<Resource<RequestOtpRes>>

    suspend fun createCustomers(customersRes: CustomersRequest): Flow<Resource<CustomersRes>>

    suspend fun requestCustomerLogin(customersRes: RequestOtpReq): Flow<Resource<CustomersRes>>

    suspend fun requestAddAddress(customerId: Int,addAddressReq: AddAddressReq): Flow<Resource<AddAddressRes>>

    suspend fun requestRemoveAddress(customerId: Int,addAddressReq: AddAddressReq): Flow<Resource<AddAddressRes>>

    suspend fun requestUpdateAddress(customerId: Int,addAddressReq: AddAddressReq): Flow<Resource<AddAddressRes>>

    suspend fun requestAddress(customerId: Int,phoneNumber : String): Flow<Resource<AddressRes>>

    suspend fun requestOrderId(orderReq: OrderReq): Flow<Resource<OrderRes>>

    suspend fun requestPaymentStatus(paymentStatusReq: PaymentStatusReq): Flow<Resource<PaymentStatusRes>>

    suspend fun requestDeliveryBoyLogin(addItemReq: RequestOtpReq): Flow<Resource<RequestOtpRes>>

    suspend fun requestDeliveryBoyScheduleOrders(deliveryBoyId: Int): Flow<Resource<DeliveryBoyOrders>>

    suspend fun requestDeliveryBoyAllOrders(deliveryBoyId: Int, all : Boolean): Flow<Resource<DeliveryBoyOrders>>

    suspend fun requestCustomerOrders(phoneNumber: String): Flow<Resource<OrdersRes>>

    suspend fun requestUpdateOrderStatus(updateOrderStatus : UpdateOrderStatus): Flow<Resource<UpdateOrderStatusRes>>
}