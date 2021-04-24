package com.jagadish.freshmart.view.deliveryboy.ui.dashboard

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jagadish.freshmart.base.BaseViewModel
import com.jagadish.freshmart.data.DataRepositorySource
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.AddItemRes
import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
import com.jagadish.freshmart.utils.SingleEvent
import com.jagadish.freshmart.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel() {




    //TODO check to make them as one Resource
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipeSearchFoundPrivate: MutableLiveData<DeliveryBoyOrders> = MutableLiveData()
    val recipeSearchFound: LiveData<DeliveryBoyOrders> get() = recipeSearchFoundPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val noSearchFoundPrivate: MutableLiveData<Unit> = MutableLiveData()
    val noSearchFound: LiveData<Unit> get() = noSearchFoundPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val openRecipeDetailsPrivate = MutableLiveData<SingleEvent<DeliveryBoyOrders>>()
    val openRecipeDetails: LiveData<SingleEvent<DeliveryBoyOrders>> get() = openRecipeDetailsPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val checkItemsInCartPrivate = MutableLiveData<SingleEvent<AddItemRes>>()
    val openCartView: LiveData<SingleEvent<AddItemRes>> get() = checkItemsInCartPrivate

    /**
     * Error handling as UI
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate



    fun openRecipeDetails(recipe: DeliveryBoyOrders) {
        openRecipeDetailsPrivate.value = SingleEvent(recipe)
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun checkCartItems(recipe: AddItemRes){
        checkItemsInCartPrivate.value = SingleEvent(recipe)
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val addressLiveDataPrivate = MutableLiveData<Resource<DeliveryBoyOrders>>()
    val addressLiveData: LiveData<Resource<DeliveryBoyOrders>> get() = addressLiveDataPrivate

    fun fetchScheduleOrders() {
        viewModelScope.launch {
            addressLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestDeliveryBoyScheduleOrders(
                    SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_USER_ID)
                ).collect {
                    addressLiveDataPrivate.value = it
                }
            }
        }
    }

    fun fetchAllOrders() {
        viewModelScope.launch {
            addressLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestDeliveryBoyAllOrders(
                    SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_USER_ID),true
                ).collect {
                    addressLiveDataPrivate.value = it
                }
            }
        }
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val onItemClickLiveDataPrivate = MutableLiveData<SingleEvent<ScheduleOrders>>()
    val onItemClickLiveData: LiveData<SingleEvent<ScheduleOrders>> get() = onItemClickLiveDataPrivate
    fun onClickOrderInfo(scheduleOrders: ScheduleOrders){
        onItemClickLiveDataPrivate.value = SingleEvent(scheduleOrders)
    }




}