package com.jagadish.freshmart.data.remote.service

import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.address.AddAddressRes
import com.jagadish.freshmart.data.dto.address.AddressRes
import com.jagadish.freshmart.data.dto.cart.*
import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatus
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatusRes
import com.jagadish.freshmart.data.dto.globalsearch.GlobalSearch
import com.jagadish.freshmart.data.dto.login.CustomersRequest
import com.jagadish.freshmart.data.dto.login.CustomersRes
import com.jagadish.freshmart.data.dto.login.RequestOtpReq
import com.jagadish.freshmart.data.dto.login.RequestOtpRes
import com.jagadish.freshmart.data.dto.order.*
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.shop.Shop
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Jagadeesh on 01-03-2021.
 */
interface RecipesService {
    @POST("cart")
    suspend fun createCart(@Body pinCode: CreateCareReq): Response<CreateCartRes>

    @GET("home?")
    suspend fun fetchRecipes(@Query(  "pincode") pinCode: String,@Query("client_version") version : String,@Query("os_type") osType : String): Response<Shop>

    @GET("products?")
    suspend fun fetchProducts(@Query("category_id") categoryId: Int,@Query("client_version") version : String,@Query("os_type") osType : String): Response<Products>

    @GET("cart/{id}/cart_info?")
    suspend fun fetchCart(@Path(  "id") cartId: Int,@Query("client_version") version : String,@Query("os_type") osType : String): Response<Cart>

    @POST("cart/add_item")
    suspend fun addItemToCart(@Body requestData: AddItemReq): Response<AddItemRes>

    @POST("cart/remove_item")
    suspend fun removeItemToCart(@Body requestData: AddItemReq): Response<AddItemRes>

    @POST("customers/request_otp")
    suspend fun requestOTP(@Body requestOtp : RequestOtpReq): Response<RequestOtpRes>

    @POST("drivers/login")
    suspend fun requestDeliveryBoyLogin(@Body requestOtp : RequestOtpReq): Response<RequestOtpRes>

    @POST("customers")
    suspend fun requestCustomers(@Body customerReq : CustomersRequest): Response<CustomersRes>

    @POST("customers/login")
    suspend fun requestCustomerLogin(@Body customerReq : RequestOtpReq): Response<CustomersRes>

    @POST("customers/{Id}/add_address")
    suspend fun requestAddAddress(@Path("Id") customerId : Int,@Body customerReq : AddAddressReq): Response<AddAddressRes>

    @POST("customers/remove_address")
    suspend fun requestRemoveAddress(@Body customerReq : AddAddressReq): Response<AddAddressRes>

    @POST("customers/update_address")
    suspend fun requestUpdateAddress(@Body customerReq : AddAddressReq): Response<AddAddressRes>


    @GET("customers/id/get_address")
    suspend fun requestAddress(@Query("customer_id") categoryId: Int, @Query("phone_number") phoneNumber : String): Response<AddressRes>

    @POST("orders")
    suspend fun requestOrderId(@Body orderReq : OrderReq,@Query("client_version") version : String,@Query("os_type") osType : String): Response<OrderRes>

    @POST("orders/update_payment_status")
    suspend fun requestPaymentStatus(@Body paymentStatusReq : PaymentStatusReq): Response<PaymentStatusRes>

    @POST("drivers/update_order_status")
    suspend fun requestUpdateOrderStatus(@Body updateOrderStatus : UpdateOrderStatus): Response<UpdateOrderStatusRes>

    @GET("drivers/orders?")
    suspend fun requestDeliveryBoyScheduleOrders(@Query("id") customerId : Int): Response<DeliveryBoyOrders>

    @GET("drivers/orders?")
    suspend fun requestDeliveryBoyAllOrders(@Query("id") customerId : Int ,@Query("all") all : Boolean): Response<DeliveryBoyOrders>

    @GET("customers/orders?")
    suspend fun requestCustomerOrders(@Query("phone_number") phone_number : String): Response<OrdersRes>

    @GET("search?")
    suspend fun requestGlobalSearch(@Query("keyword") keyword : String): Response<GlobalSearch>

}