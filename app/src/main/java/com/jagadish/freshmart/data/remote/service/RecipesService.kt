package com.jagadish.freshmart.data.remote.service

import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.shop.Shop
import com.jagadish.freshmart.data.dto.shop.ShopItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Jagadeesh on 01-03-2021.
 */
interface RecipesService {
    @GET("home?")
    suspend fun fetchRecipes(@Query(  "pincode") pinCode: String): Response<Shop>

    @GET("products?")
    suspend fun fetchProducts(@Query("category_id") categoryId: Int): Response<Products>

    @GET("b5d3b6ff-7dac-11eb-b747-ef47dab590f4")
    suspend fun fetchCart(): Response<Cart>
}