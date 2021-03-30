package com.jagadish.freshmart.view.address

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jagadish.freshmart.base.BaseViewModel
import com.jagadish.freshmart.data.DataRepositorySource
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.address.AddAddressRes
import com.jagadish.freshmart.data.dto.address.AddressRes
import com.jagadish.freshmart.data.dto.cart.CreateCartRes
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.utils.SingleEvent
import com.jagadish.freshmart.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdressViewModel  @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel()  {
    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipesLiveDataPrivate = MutableLiveData<Resource<AddAddressRes>>()
    val recipesLiveData: LiveData<Resource<AddAddressRes>> get() = recipesLiveDataPrivate


    //TODO check to make them as one Resource
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipeSearchFoundPrivate: MutableLiveData<ProductsItem> = MutableLiveData()
    val recipeSearchFound: LiveData<ProductsItem> get() = recipeSearchFoundPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val noSearchFoundPrivate: MutableLiveData<Unit> = MutableLiveData()
    val noSearchFound: LiveData<Unit> get() = noSearchFoundPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val openRecipeDetailsPrivate = MutableLiveData<SingleEvent<AddAddressReq>>()
    val openRecipeDetails: LiveData<SingleEvent<AddAddressReq>> get() = openRecipeDetailsPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val checkItemsInCartPrivate = MutableLiveData<SingleEvent<CreateCartRes>>()
    val openCartView: LiveData<SingleEvent<CreateCartRes>> get() = checkItemsInCartPrivate

    /**
     * Error handling as UI
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


    fun requestAddAddress(addressReq:  AddAddressReq) {
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestAddAddress(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_USER_ID),addressReq).collect {
                    recipesLiveDataPrivate.value = it
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val addressLiveDataPrivate = MutableLiveData<Resource<AddressRes>>()
    val addressLiveData: LiveData<Resource<AddressRes>> get() = addressLiveDataPrivate

    fun fetchAddress() {
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestAddress(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_USER_ID),
                    SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_MOBILE)
                ).collect {
                    addressLiveDataPrivate.value = it
                }
            }
        }
    }

    fun removeAddress(addressReq: AddAddressReq) {
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestRemoveAddress(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_USER_ID),
                    addressReq
                ).collect {
                    showToastPrivate.value = SingleEvent(it.data!!.message)
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val updateDefaultAddressPrivate = MutableLiveData<SingleEvent<AddAddressReq>>()
    val updateDefaultAddressDetails: LiveData<SingleEvent<AddAddressReq>> get() = updateDefaultAddressPrivate

    fun openRecipeDetails(recipe: AddAddressReq) {
        updateDefaultAddressPrivate.value = SingleEvent(recipe)
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun updateAddressNavigate(addressReq: AddAddressReq){
        openRecipeDetailsPrivate.value = SingleEvent(addressReq)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val updateAddressPrivate: MutableLiveData<Resource<AddAddressRes>> = MutableLiveData()
    val updateAddress: LiveData<Resource<AddAddressRes>> get() = updateAddressPrivate
    fun updateAddress(addressReq: AddAddressReq) {
        viewModelScope.launch {
            updateAddressPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                addressReq.phone_number = SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_MOBILE)
                dataRepositoryRepository.requestUpdateAddress(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_USER_ID),
                    addressReq
                ).collect {
                    showToastPrivate.value = SingleEvent(it.data!!.message)
                    updateAddressPrivate.value = it
                }
            }
        }
    }
}