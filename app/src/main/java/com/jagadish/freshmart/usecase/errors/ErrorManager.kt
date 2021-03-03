package com.jagadish.freshmart.usecase.errors

import com.jagadish.freshmart.data.error.mapper.ErrorMapper
import javax.inject.Inject
import com.jagadish.freshmart.data.error.Error
/**
 * Created by Jagadeesh on 01-03-2021.
 */
class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorUseCase {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }
}