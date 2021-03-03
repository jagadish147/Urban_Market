package com.jagadish.freshmart.data.error.mapper

/**
 * Created by Jagadeesh on 01-03-2021.
 */
interface ErrorMapperSource {
    fun getErrorString(errorId: Int): String
    val errorsMap: Map<Int, String>
}