package com.jagadish.freshmart.data.remote

import com.jagadish.freshmart.data.Resource
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

    override suspend fun requestCreateCart(fcm_token: CreateCareReq): Resource<CreateCartRes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({ recipesService.createCart(fcm_token) })) {
            is CreateCartRes -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }


    override suspend fun requestRecipes(pinCode:String): Resource<Shop> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({ recipesService.fetchRecipes(pinCode) })) {
            is Shop -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestProducts(categoryId: Int): Resource<Products> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.fetchProducts(categoryId)})) {
            is Products -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestCart(cartId: Int): Resource<Cart> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.fetchCart(cartId)})) {
            is Cart -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestAddItem(addItemReq: AddItemReq) : Resource<AddItemRes>{
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.addItemToCart(addItemReq)})) {
            is AddItemRes -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestRemoveItem(addItemReq: AddItemReq): Resource<AddItemRes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.removeItemToCart(addItemReq)})) {
            is AddItemRes -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestOTP(addItemReq: RequestOtpReq) : Resource<RequestOtpRes>{
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.requestOTP(addItemReq)})) {
            is RequestOtpRes -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun createCustomers(customersRes: CustomersRequest): Resource<CustomersRes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.requestCustomers(customersRes)})) {
            is CustomersRes -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestCustomerLogin(customersRes: RequestOtpReq): Resource<CustomersRes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.requestCustomerLogin(customersRes)})) {
            is CustomersRes -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestAddAddress(
        customerId: Int,
        addAddressReq: AddAddressReq
    ): Resource<AddAddressRes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.requestAddAddress(customerId,addAddressReq)})) {
            is AddAddressRes -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestRemoveAddress(
        customerId: Int,
        addAddressReq: AddAddressReq
    ): Resource<AddAddressRes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.requestRemoveAddress(customerId,addAddressReq)})) {
            is AddAddressRes -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestUpdateAddress(
        customerId: Int,
        addAddressReq: AddAddressReq
    ): Resource<AddAddressRes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.requestUpdateAddress(addAddressReq)})) {
            is AddAddressRes -> {
                Resource.Success(data = response )
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestAddress(customerId: Int,phoneNumber : String): Resource<AddressRes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall({recipesService.requestAddress(customerId,phoneNumber)})) {
            is AddressRes -> {
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