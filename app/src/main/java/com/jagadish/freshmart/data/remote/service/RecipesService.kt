package com.jagadish.freshmart.data.remote.service

import com.jagadish.freshmart.data.dto.shop.Shop
import com.jagadish.freshmart.data.dto.shop.ShopItem
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Jagadeesh on 01-03-2021.
 */
interface RecipesService {
    @GET("eccd16e6-7a2e-11eb-becc-a3dfce0ac389")
    suspend fun fetchRecipes(): Response<Shop>
}