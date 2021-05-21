package com.jagadish.freshmart.view.main.ui.orders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.*
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.cart.AddItemRes
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatusRes
import com.jagadish.freshmart.data.dto.order.OrderItems
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentOrderSummaryBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.main.ui.cart.CartViewModel
import com.jagadish.freshmart.view.main.ui.orders.adapter.OrderSummaryAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the defaultAddress destination in the navigation.
 */
@AndroidEntryPoint
class OrderSummaryFragment : BaseFragment() {

    private lateinit var binding: FragmentOrderSummaryBinding
    private val recipesListViewModel: CartViewModel by viewModels()
    private lateinit var recipesAdapter: OrderSummaryAdapter
    private val args: OrderSummaryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderSummaryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        val layoutManager = LinearLayoutManager(context)
        binding.cartItemsRecyclerView.layoutManager = layoutManager
        binding.cartItemsRecyclerView.setHasFixedSize(true)
//        recipesListViewModel.getRecipes()
        if(args.orderSummery.id !=0){

            val orderInfo = args.orderSummery
            orderInfo?.let {
                bindListData(it) }
        }

        binding.backArrowBtn.setOnClickListener {
            findNavController().navigate(
                OrderSummaryFragmentDirections.actionNavigationOrderSummaryToNavigationOrders(args.orderSummery)
            )

        }

        binding.okBtn.setOnClickListener {
            findNavController().navigate(
                OrderSummaryFragmentDirections.actionNavigationOrderSummaryToNavigationOrders(args.orderSummery)
            )

        }

    }

    private fun observeViewModel() {
        observe(recipesListViewModel.orderStatusLiveData, ::handleRecipesList)
        observe(recipesListViewModel.recipeSearchFound, ::showSearchResult)
        observe(recipesListViewModel.noSearchFound, ::noSearchResult)
        observeEvent(recipesListViewModel.openRecipeDetails, ::navigateToDetailsScreen)
        observeEvent(recipesListViewModel.openCartView, ::showCartView)
        observeSnackBarMessages(recipesListViewModel.showSnackBar)
        observeToast(recipesListViewModel.showToast)

    }


    private fun handleSearch(query: String) {
        if (query.isNotEmpty()) {
            binding.pbLoading.visibility = View.VISIBLE
        }
    }


    private fun bindListData(recipes: OrderItems) {
        if (!(recipes.items.isNullOrEmpty())) {
            recipesAdapter = OrderSummaryAdapter(recipesListViewModel, recipes.items)
            binding.cartItemsRecyclerView.adapter = recipesAdapter
            binding.order = recipes
            binding.orderInfoLayout.toVisible()
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<ProductsItem>) {
//        navigateEvent.getContentIfNotHandled()?.let {
//            val nextScreenIntent = Intent(requireActivity(), OrderInfoActivity::class.java).apply {
//                putExtra(CATEGORY_KEY, it)
//            }
//            startActivity(nextScreenIntent)
//        }
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

    private fun handleRecipesList(status: Resource<UpdateOrderStatusRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { updateOrderStatusSuccess(updateOrderStatusRes = it) }
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

    private fun updateOrderStatusSuccess(updateOrderStatusRes: UpdateOrderStatusRes){
        val data = Intent().apply {
            putExtra(RESULT_ACTIVITY_ORDER_STATUS, true)
        }
        requireActivity().setResult(Activity.RESULT_OK, data)
        requireActivity().finish()
    }

    fun onClickBack(){

    }


}