package com.jagadish.freshmart.base

import androidx.lifecycle.ViewModel
import com.jagadish.freshmart.usecase.errors.ErrorManager
import javax.inject.Inject

/**
 * Created by Jagadeesh on 01-03-2021.
 */
abstract class BaseViewModel : ViewModel() {
    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    @Inject
    lateinit var errorManager: ErrorManager
}