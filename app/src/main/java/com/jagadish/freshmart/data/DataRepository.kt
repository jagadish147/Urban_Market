package com.jagadish.freshmart.data

import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.shop.Shop
import com.jagadish.freshmart.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Jagadeesh on 01-03-2021.
 */
class DataRepository @Inject constructor(private val remoteRepository: RemoteData, private val ioDispatcher: CoroutineContext) : DataRepositorySource {

    override suspend fun requestRecipes(pinCode:String): Flow<Resource<Shop>> {
        return flow {
            emit(remoteRepository.requestRecipes(pinCode))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestProducts(categoryId: Int): Flow<Resource<Products>> {
        return flow {
            emit(remoteRepository.requestProducts(categoryId))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestCart(): Flow<Resource<Cart>> {
        return flow {
            emit(remoteRepository.requestCart())
        }.flowOn(ioDispatcher)
    }


}