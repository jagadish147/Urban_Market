package com.jagadish.freshmart.view.main.ui.cart

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jagadish.freshmart.base.BaseViewModel
import com.jagadish.freshmart.data.DataRepositorySource
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.address.AddressRes
import com.jagadish.freshmart.data.dto.cart.AddItemReq
import com.jagadish.freshmart.data.dto.cart.AddItemRes
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatus
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatusRes
import com.jagadish.freshmart.data.dto.order.OrderReq
import com.jagadish.freshmart.data.dto.order.OrderRes
import com.jagadish.freshmart.data.dto.order.PaymentStatusReq
import com.jagadish.freshmart.data.dto.order.PaymentStatusRes
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.utils.SingleEvent
import com.jagadish.freshmart.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel() {

    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipesLiveDataPrivate = MutableLiveData<Resource<Cart>>()
    val recipesLiveData: LiveData<Resource<Cart>> get() = recipesLiveDataPrivate


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
    private val openRecipeDetailsPrivate = MutableLiveData<SingleEvent<ProductsItem>>()
    val openRecipeDetails: LiveData<SingleEvent<ProductsItem>> get() = openRecipeDetailsPrivate

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


    fun getRecipes() {
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestCart(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID)).collect {
                    recipesLiveDataPrivate.value = it
                }
            }
        }
    }

    fun openRecipeDetails(recipe: ProductsItem) {
        openRecipeDetailsPrivate.value = SingleEvent(recipe)
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun checkCartItems(recipe: AddItemRes, productsItem: ProductsItem){
        checkItemsInCartPrivate.value = SingleEvent(recipe)
    }

    fun checkCartItemsSze(size: Int) {
        updatePaymentViewPrivate.value = SingleEvent(size)
    }
    fun addCartItem(productsItem: ProductsItem,categoryId: AddItemReq) {
        viewModelScope.launch {
//            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestAddItem(categoryId).collect {
                    removeCartItemPrivate.value = SingleEvent(productsItem)
                    showToastPrivate.value = SingleEvent(it.data!!.message)
                    checkCartItems(it.data,productsItem)
                }
            }
        }
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val updatePaymentViewPrivate = MutableLiveData<SingleEvent<Int>>()
    val updatePaymentViewItem: LiveData<SingleEvent<Int>> get() = updatePaymentViewPrivate

    fun removeCartItem(productsItem: ProductsItem,categoryId: AddItemReq) {
        viewModelScope.launch {
//            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestRemoveItem(categoryId).collect {
                    removeCartItemPrivate.value = SingleEvent(productsItem)
                    showToastPrivate.value = SingleEvent(it.data!!.message)
                    checkCartItems(it.data, productsItem)
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val orderIdLiveDataPrivate = MutableLiveData<Resource<OrderRes>>()
    val orderIdLiveData: LiveData<Resource<OrderRes>> get() = orderIdLiveDataPrivate

    fun createOrderId(addressId: Int) {
        viewModelScope.launch {
            orderIdLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestOrderId(
                    OrderReq( SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID),SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_USER_ID),
                    addressId)
                ).collect {
                    orderIdLiveDataPrivate.value = it
                }
            }
        }
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
    private val removeCartItemPrivate = MutableLiveData<SingleEvent<ProductsItem>>()
    val removeCartItem: LiveData<SingleEvent<ProductsItem>> get() = removeCartItemPrivate
}