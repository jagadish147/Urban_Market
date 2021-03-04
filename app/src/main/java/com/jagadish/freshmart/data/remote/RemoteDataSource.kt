package com.jagadish.freshmart.data.remote

import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.shop.Shop

/**
 * Created by Jagadeesh on 01-03-2021.
 */
internal interface RemoteDataSource {
    suspend fun requestRecipes(): Resource<Shop>

    suspend fun requestProducts(): Resource<Products>
}