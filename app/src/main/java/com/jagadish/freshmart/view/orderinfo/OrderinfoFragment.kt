package com.jagadish.freshmart.view.orderinfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.CATEGORY_KEY
import com.jagadish.freshmart.R
import com.jagadish.freshmart.RESULT_ACTIVITY_IS_VIEW_CART
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.cart.AddItemRes
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentOrderInfoBinding
import com.jagadish.freshmart.databinding.FragmentProductsBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.main.ui.cart.CartViewModel
import com.jagadish.freshmart.view.main.ui.cart.adapter.CartItemsAdapter
import com.jagadish.freshmart.view.orderinfo.adapter.OrderinfoAdapter
import com.jagadish.freshmart.view.products.ProductsFragmentViewModel
import com.jagadish.freshmart.view.products.ProductsListActivity
import com.jagadish.freshmart.view.products.adapter.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the defaultAddress destination in the navigation.
 */
@AndroidEntryPoint
class OrderinfoFragment : BaseFragment() {

    private lateinit var binding: FragmentOrderInfoBinding
    private val recipesListViewModel: CartViewModel by viewModels()
    private lateinit var recipesAdapter: OrderinfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        val layoutManager = LinearLayoutManager(context)
        binding.cartItemsRecyclerView.layoutManager = layoutManager
        binding.cartItemsRecyclerView.setHasFixedSize(true)
        recipesListViewModel.getRecipes()

        binding.orderConfirmBtn.setOnClickListener {
            val nextScreenIntent = Intent(requireActivity(), OrderInfoActivity::class.java).apply {
//                putExtra(CATEGORY_KEY, it)
            }
            startActivity(nextScreenIntent)
        }
    }

    private fun observeViewModel() {
        observe(recipesListViewModel.recipesLiveData, ::handleRecipesList)
        observe(recipesListViewModel.recipeSearchFound, ::showSearchResult)
        observe(recipesListViewModel.noSearchFound, ::noSearchResult)
        observeEvent(recipesListViewModel.openRecipeDetails, ::navigateToDetailsScreen)
        observeEvent(recipesListViewModel.openCartView, ::showCartView )
        observeSnackBarMessages(recipesListViewModel.showSnackBar)
        observeToast(recipesListViewModel.showToast)

    }


    private fun handleSearch(query: String) {
        if (query.isNotEmpty()) {
            binding.pbLoading.visibility = View.VISIBLE
        }
    }


    private fun bindListData(recipes: Cart) {
        if (!(recipes.products.isNullOrEmpty())) {
            recipesAdapter = OrderinfoAdapter(recipesListViewModel,recipes.products )
            binding.cartItemsRecyclerView.adapter = recipesAdapter
            binding.order = recipes
            binding.orderInfoLayout.toVisible()
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<ProductsItem>) {
        navigateEvent.getContentIfNotHandled()?.let {
            val nextScreenIntent = Intent(requireActivity(), OrderInfoActivity::class.java).apply {
                putExtra(CATEGORY_KEY, it)
            }
            startActivity(nextScreenIntent)
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

    private fun showDataView(show: Boolean) {
        binding.tvNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.cartItemsRecyclerView.visibility = if (show) View.VISIBLE else View.GONE
        binding.pbLoading.toGone()
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.cartItemsRecyclerView.toGone()
    }


    private fun showSearchResult(recipesItem: ProductsItem) {
        recipesListViewModel.openRecipeDetails(recipesItem)
        binding.pbLoading.toGone()
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()
        binding.pbLoading.toGone()
    }

    private fun handleRecipesList(status: Resource<Cart>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(recipes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { recipesListViewModel.showToastMessage(it) }
            }
        }
    }

    private fun showCartView(navigateEvent: SingleEvent<AddItemRes>) {
        navigateEvent.getContentIfNotHandled()?.let {

        }
    }
}