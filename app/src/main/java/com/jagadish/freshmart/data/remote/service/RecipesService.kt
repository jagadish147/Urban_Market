package com.jagadish.freshmart.data.remote.service

import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.shop.Shop
import com.jagadish.freshmart.data.dto.shop.ShopItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by Jagadeesh on 01-03-2021.
 */
interface RecipesService {
    @GET("eccd16e6-7a2e-11eb-becc-a3dfce0ac389")
    suspend fun fetchRecipes(): Response<Shop>

    @GET("fdf740f5-7c8b-11eb-981f-f1174c66c577")
    suspend fun fetchProducts(): Response<Products>
}