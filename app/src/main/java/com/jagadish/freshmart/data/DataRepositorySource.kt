package com.jagadish.freshmart.data

import com.jagadish.freshmart.data.dto.shop.Shop
import kotlinx.coroutines.flow.Flow

/**
 * Created by Jagadeesh on 01-03-2021.
 */
interface DataRepositorySource {
    suspend fun requestRecipes(): Flow<Resource<Shop>>

}