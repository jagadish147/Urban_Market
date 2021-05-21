package com.jagadish.freshmart.view.main.ui.store

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jagadish.freshmart.base.BaseViewModel
import com.jagadish.freshmart.data.DataRepositorySource
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.globalsearch.GlobalSearch
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.Shop
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.utils.SingleEvent
import com.jagadish.freshmart.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class StoreViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel() {

    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipesLiveDataPrivate = MutableLiveData<Resource<Shop>>()
    val recipesLiveData: LiveData<Resource<Shop>> get() = recipesLiveDataPrivate


    //TODO check to make them as one Resource
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipeSearchFoundPrivate: MutableLiveData<Resource<GlobalSearch>> = MutableLiveData()
    val recipeSearchFound: LiveData<Resource<GlobalSearch>> get() = recipeSearchFoundPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val noSearchFoundPrivate: MutableLiveData<Unit> = MutableLiveData()
    val noSearchFound: LiveData<Unit> get() = noSearchFoundPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val openRecipeDetailsPrivate = MutableLiveData<SingleEvent<ShopItem>>()
    val openRecipeDetails: LiveData<SingleEvent<ShopItem>> get() = openRecipeDetailsPrivate

    /**
     * Error handling as UI
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


    fun getRecipes(pinCode:String) {
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestRecipes(pinCode).collect {
                    recipesLiveDataPrivate.value = it
                }
            }
        }
    }

    fun openRecipeDetails(recipe: ShopItem) {
        openRecipeDetailsPrivate.value = SingleEvent(recipe)
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val openProductDetailsPrivate = MutableLiveData<SingleEvent<ProductsItem>>()
    val openProductDetails: LiveData<SingleEvent<ProductsItem>> get() = openProductDetailsPrivate
    fun openProductDetailsPage(recipesItem: ProductsItem){
        openProductDetailsPrivate.value = SingleEvent(recipesItem)
    }
    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun searchProducts(searchQuarry : String){
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestGlobalSearch(searchQuarry).collect {
                    recipeSearchFoundPrivate.value = it
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val localSearchFoundPrivate: MutableLiveData<GlobalSearch> = MutableLiveData()
    val localSearchFound: LiveData<GlobalSearch> get() = localSearchFoundPrivate

    fun localGlobalSearch(searchQuarry: String){
        if (searchQuarry.isNotEmpty()) {
            if(recipeSearchFoundPrivate.value !=null) {
                var products = ArrayList<ProductsItem>()
                if (recipeSearchFoundPrivate.value?.data?.products?.size!! > 0) {
                    for (item in recipeSearchFoundPrivate.value?.data?.products!!) {
                        if (item.name.toUpperCase(Locale.getDefault()).contains(
                                searchQuarry.toUpperCase(
                                    Locale.getDefault()
                                )
                            )
                        ) {
                            products.add(item)
                        }
                    }
                }
                var categories = ArrayList<ShopItem>()
                if (recipeSearchFoundPrivate.value?.data?.categories?.size!! > 0) {
                    for (item in recipeSearchFoundPrivate.value?.data?.categories!!) {
                        if (item.name.toUpperCase(Locale.getDefault()).contains(
                                searchQuarry.toUpperCase(
                                    Locale.getDefault()
                                )
                            )
                        ) {
                            categories.add(item)
                        }
                    }
                }
                localSearchFoundPrivate.value =
                    GlobalSearch(200, "success", products, categories, true)

            }
        } else {
            noSearchFoundPrivate.value = Unit
        }
    }
}