package com.jagadish.freshmart.view.payment

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jagadish.freshmart.base.BaseViewModel
import com.jagadish.freshmart.data.DataRepositorySource
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatus
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatusRes
import com.jagadish.freshmart.data.dto.order.OrderReq
import com.jagadish.freshmart.data.dto.order.OrderRes
import com.jagadish.freshmart.data.dto.order.PaymentStatusReq
import com.jagadish.freshmart.data.dto.order.PaymentStatusRes
import com.jagadish.freshmart.utils.SingleEvent
import com.jagadish.freshmart.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderPlaceViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel(){
    /**
     * Error handling as UI
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val paymentStatusLiveDataPrivate = MutableLiveData<Resource<PaymentStatusRes>>()
    val paymentStatusLiveData: LiveData<Resource<PaymentStatusRes>> get() = paymentStatusLiveDataPrivate

    fun checkPaymentStatus(paymentStatusReq: PaymentStatusReq) {
        viewModelScope.launch {
            paymentStatusLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestPaymentStatus(
                    paymentStatusReq
                ).collect {
                    paymentStatusLiveDataPrivate.value = it
                }
            }
        }
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val orderStatusLiveDataPrivate = MutableLiveData<Resource<UpdateOrderStatusRes>>()
    val orderStatusLiveData: LiveData<Resource<UpdateOrderStatusRes>> get() = orderStatusLiveDataPrivate

    fun updateOrderStatus(updateOrderStatus: UpdateOrderStatus) {
        viewModelScope.launch {
            orderStatusLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestUpdateOrderStatus(
                    updateOrderStatus
                ).collect {
                    orderStatusLiveDataPrivate.value = it
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val orderIdLiveDataPrivate = MutableLiveData<Resource<OrderRes>>()
    val orderIdLiveData: LiveData<Resource<OrderRes>> get() = orderIdLiveDataPrivate

    fun createOrderId(addressId: Int) {
        viewModelScope.launch {
            orderIdLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestOrderId(
                    OrderReq( SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID),
                        SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_USER_ID),
                        addressId)
                ).collect {
                    orderIdLiveDataPrivate.value = it
                }
            }
        }
    }
}