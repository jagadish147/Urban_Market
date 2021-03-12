package com.jagadish.freshmart.data.remote

import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.shop.Shop

/**
 * Created by Jagadeesh on 01-03-2021.
 */
internal interface RemoteDataSource {
    suspend fun requestRecipes(pinCode:String): Resource<Shop>

    suspend fun requestProducts(categoryId: Int): Resource<Products>

    suspend fun requestCart(): Resource<Cart>
}