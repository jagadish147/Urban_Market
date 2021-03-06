package com.jagadish.freshmart.data.remote

import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.shop.Shop
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.data.error.NETWORK_ERROR
import com.jagadish.freshmart.data.error.NO_INTERNET_CONNECTION
import com.jagadish.freshmart.data.remote.service.RecipesService
import com.jagadish.freshmart.utils.NetworkConnectivity
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Jagadeesh on 01-03-2021.
 */
class RemoteData @Inject
constructor(private val serviceGenerator: ServiceGenerator, private val networkConnectivity: NetworkConnectivity) : RemoteDataSource {
    override suspend fun requestRecipes(): Resource<Shop> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall(recipesService::fetchRecipes)) {
            is Shop -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestProducts(): Resource<Products> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall(recipesService::fetchProducts)) {
            is Products -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestCart(): Resource<Cart> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall(recipesService::fetchCart)) {
            is Cart -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }
}