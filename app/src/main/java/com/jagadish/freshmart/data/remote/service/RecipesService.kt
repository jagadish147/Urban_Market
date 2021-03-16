package com.jagadish.freshmart.data.remote.service

import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.address.AddAddressRes
import com.jagadish.freshmart.data.dto.address.AddressRes
import com.jagadish.freshmart.data.dto.address.GetAddressReq
import com.jagadish.freshmart.data.dto.cart.*
import com.jagadish.freshmart.data.dto.login.CustomersRequest
import com.jagadish.freshmart.data.dto.login.CustomersRes
import com.jagadish.freshmart.data.dto.login.RequestOtpReq
import com.jagadish.freshmart.data.dto.login.RequestOtpRes
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
    suspend fun fetchRecipes(@Query(  "pincode") pinCode: String): Response<Shop>

    @GET("products?")
    suspend fun fetchProducts(@Query("category_id") categoryId: Int): Response<Products>

    @GET("cart/{id}/cart_info?")
    suspend fun fetchCart(@Path(  "id") cartId: Int): Response<Cart>

    @POST("cart/add_item")
    suspend fun addItemToCart(@Body requestData: AddItemReq): Response<AddItemRes>

    @POST("cart/remove_item")
    suspend fun removeItemToCart(@Body requestData: AddItemReq): Response<AddItemRes>

    @POST("customers/request_otp")
    suspend fun requestOTP(@Body requestOtp : RequestOtpReq): Response<RequestOtpRes>

    @POST("customers")
    suspend fun requestCustomers(@Body customerReq : CustomersRequest): Response<CustomersRes>

    @POST("customers/login")
    suspend fun requestCustomerLogin(@Body customerReq : RequestOtpReq): Response<CustomersRes>

    @POST("customers/{Id}/add_address")
    suspend fun requestAddAddress(@Path("Id") customerId : Int,@Body customerReq : AddAddressReq): Response<AddAddressRes>

    @POST("customers/{Id}/remove_address")
    suspend fun requestRemoveAddress(@Path("Id") customerId : Int,@Body customerReq : AddAddressReq): Response<AddAddressRes>


    @GET("customers/id/get_address")
    suspend fun requestAddress(@Query("customer_id") categoryId: Int, @Query("phone_number") phoneNumber : String): Response<AddressRes>
}