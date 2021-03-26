package com.jagadish.freshmart.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.BuildConfig
import com.jagadish.freshmart.CATEGORY_KEY
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.cart.CreateCareReq
import com.jagadish.freshmart.data.dto.cart.CreateCartRes
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.ActivitySplashBinding
import com.jagadish.freshmart.databinding.FragmentProductsBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.address.AddressActivity
import com.jagadish.freshmart.view.deliveryboy.DeliveryHomeActivity
import com.jagadish.freshmart.view.deliveryboy.login.DeliveryBoyLoginActivity
import com.jagadish.freshmart.view.intro.IntroSliderActivity
import com.jagadish.freshmart.view.login.LoginActivity
import com.jagadish.freshmart.view.main.MainActivity
import com.jagadish.freshmart.view.products.ProductsFragmentViewModel
import com.jagadish.freshmart.view.products.ProductsListActivity
import com.jagadish.freshmart.view.products.adapter.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val recipesListViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferencesUtils.init(this)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        observeViewModel()

        if(BuildConfig.FLAVOR == "user") {
            if (!SharedPreferencesUtils.getStringPreference(
                    SharedPreferencesUtils.PREF_DEVICE_CART
                ).isNullOrEmpty()
            ) {
                binding.pbLoading.toGone()
                recipesListViewModel.getCartItems()
            } else {
                checkFCM()
            }
        }else{
            Handler().postDelayed({
                navigateDeliveryBoy()
            }, 2000)
//            if (!SharedPreferencesUtils.getStringPreference(
//                    SharedPreferencesUtils.PREF_DEVICE_CART
//                ).isNullOrEmpty()
//            ) {
//                binding.pbLoading.toGone()
//                recipesListViewModel.getCartItems()
//            } else {
//                checkFCM()
//            }
        }
    }

    private fun checkFCM(){
        if(!TextUtils.isEmpty(SharedPreferencesUtils.getAppStringPreference(SharedPreferencesUtils.PREF_APP_FCM_TOKEN))) {
            recipesListViewModel.createCart(
                CreateCareReq(
                    SharedPreferencesUtils.getAppStringPreference(
                        SharedPreferencesUtils.PREF_APP_FCM_TOKEN
                    )
                )
            )

        }else{
            Handler().postDelayed({
                checkFCM()
            }, 2000)

        }
    }

    private fun observeViewModel() {
        observe(recipesListViewModel.recipesLiveData, ::handleRecipesList)
        observe(recipesListViewModel.recipeSearchFound, ::showSearchResult)
        observe(recipesListViewModel.noSearchFound, ::noSearchResult)
        observeSnackBarMessages(recipesListViewModel.showSnackBar)
        observeToast(recipesListViewModel.showToast)
        observe(recipesListViewModel.cartLiveDataPrivate, ::handleCartList)
    }


    private fun bindListData(cart: CreateCartRes) {
        if(cart.success) {
            SharedPreferencesUtils.setStringPreference(
                SharedPreferencesUtils.PREF_DEVICE_CART,
                cart.cart_id
            )
            SharedPreferencesUtils.setIntPreference(
                SharedPreferencesUtils.PREF_DEVICE_CART_ID,
                cart.id
            )

            navigateHome()
        }else{

        }
    }


    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun showSearchError() {
        recipesListViewModel.showToastMessage(SEARCH_ERROR)
    }



    private fun showLoadingView() {
        binding.pbLoading.toVisible()
    }


    private fun showSearchResult(recipesItem: ProductsItem) {
        recipesListViewModel.openRecipeDetails(recipesItem)
        binding.pbLoading.toGone()
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()
        binding.pbLoading.toGone()
    }

    private fun handleRecipesList(status: Resource<CreateCartRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(it) }
            is Resource.DataError -> {
                status.errorCode?.let { recipesListViewModel.showToastMessage(it) }
            }
        }
    }
    private fun handleCartList(status: Resource<Cart>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { storeCartItems(recipes = it) }
            is Resource.DataError -> {
                status.errorCode?.let { recipesListViewModel.showToastMessage(it) }
            }
        }
    }

    private fun storeCartItems(recipes: Cart){
        Singleton.getInstance().cart = recipes
        navigateHome()
    }
    private fun navigateHome(){
        if (!SharedPreferencesUtils.getAppBooleanPreference(SharedPreferencesUtils.PREF_APP_FIRST_TIME)) {
            startActivity(Intent(this@SplashActivity, IntroSliderActivity::class.java))
        } else {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
        finish()
    }

    private fun navigateDeliveryBoy(){
        if (SharedPreferencesUtils.getBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN)) {
            startActivity(Intent(this@SplashActivity, DeliveryHomeActivity::class.java))
        }else{
            startActivity(Intent(this@SplashActivity, DeliveryBoyLoginActivity::class.java))
        }
        finish()
    }
}