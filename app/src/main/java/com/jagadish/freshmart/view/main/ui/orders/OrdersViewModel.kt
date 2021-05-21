package com.jagadish.freshmart.view.main.ui.orders

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadish.freshmart.base.BaseViewModel
import com.jagadish.freshmart.data.DataRepositorySource
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.order.OrderItems
import com.jagadish.freshmart.data.dto.order.OrdersRes
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.utils.SingleEvent
import com.jagadish.freshmart.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel  @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel(){

    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipesLiveDataPrivate = MutableLiveData<Resource<OrdersRes>>()
    val recipesLiveData: LiveData<Resource<OrdersRes>> get() = recipesLiveDataPrivate

    /**
     * Error handling as UI
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


    fun getOrders() {
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestCustomerOrders(
                    SharedPreferencesUtils.getStringPreference(
                        SharedPreferencesUtils.PREF_USER_MOBILE)).collect {
                    recipesLiveDataPrivate.value = it
                }
            }
        }
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val orderItemPrivate = MutableLiveData<SingleEvent<OrderItems>>()
    val orderItemClick: LiveData<SingleEvent<OrderItems>> get() = orderItemPrivate

    fun onClickOrderItem(orderItems: OrderItems){
        orderItemPrivate.value = SingleEvent(orderItems)
    }
}