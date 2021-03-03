package com.jagadish.freshmart.usecase.errors

import com.jagadish.freshmart.data.error.Error

/**
 * Created by Jagadeesh on 01-03-2021.
 */
interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}