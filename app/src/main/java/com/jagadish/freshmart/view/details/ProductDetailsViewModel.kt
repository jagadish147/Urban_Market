package com.jagadish.freshmart.view.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jagadish.freshmart.base.BaseViewModel
import com.jagadish.freshmart.data.DataRepositorySource
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.cart.AddItemReq
import com.jagadish.freshmart.data.dto.cart.AddItemRes
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.utils.SingleEvent
import com.jagadish.freshmart.utils.Singleton
import com.jagadish.freshmart.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel() {

    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipesLiveDataPrivate = MutableLiveData<Resource<Products>>()
    val recipesLiveData: LiveData<Resource<Products>> get() = recipesLiveDataPrivate


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


    fun getRecipes(categoryId: Int) {
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestProducts(categoryId).collect {
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

    fun checkCartItems(recipe: AddItemRes,productsItem: ProductsItem,){
        if(recipe.success && Singleton.getInstance().cart != null ) {
            var isExistingProduct : Boolean = false
            for((index, value) in Singleton.getInstance().cart.products.withIndex()){
                if(value.id == productsItem.id){
                    isExistingProduct = true
                    if(productsItem.quantity >=1) {
                        Singleton.getInstance().cart.products[index].quantity =
                            productsItem.quantity
                        Singleton.getInstance().cart.total_price = recipe.total_price
                    }else{
                        Singleton.getInstance().cart.products.removeAt(index)
                        Singleton.getInstance().cart.total_price = recipe.total_price
                    }
                    break
                }
            }
            if(!isExistingProduct) {
                Singleton.getInstance().cart.products.add(productsItem)
                Singleton.getInstance().cart.total_price = recipe.total_price
            }
        }
        checkItemsInCartPrivate.value = SingleEvent(recipe)
    }

    fun addCartItem(productsItem: ProductsItem,categoryId: AddItemReq) {
        viewModelScope.launch {
//            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestAddItem(categoryId).collect {
                    showToastPrivate.value = SingleEvent(it.data!!.message)
                    checkCartItems(it.data,productsItem)
                }
            }
        }
    }

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val removeCartItemPrivate = MutableLiveData<SingleEvent<ProductsItem>>()
    val removeCartItem: LiveData<SingleEvent<ProductsItem>> get() = removeCartItemPrivate

    fun removeCartItem(productsItem: ProductsItem,categoryId: AddItemReq) {
        viewModelScope.launch {
//            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestRemoveItem(categoryId).collect {
                    removeCartItemPrivate.value = SingleEvent(productsItem)
                    showToastPrivate.value = SingleEvent(it.data!!.message)
                    checkCartItems(it.data,productsItem)
                }
            }
        }
    }


}