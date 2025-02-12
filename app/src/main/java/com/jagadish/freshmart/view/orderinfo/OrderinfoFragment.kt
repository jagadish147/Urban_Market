package com.jagadish.freshmart.view.orderinfo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.*
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.AddItemRes
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatus
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatusRes
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentOrderInfoBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.main.ui.cart.CartViewModel
import com.jagadish.freshmart.view.orderinfo.adapter.OrderinfoAdapter
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
//        recipesListViewModel.getRecipes()
       val orderInfo = requireActivity().intent.getParcelableExtra<ScheduleOrders>(ORDER_INFO)
        orderInfo?.let {
            bindListData(it) }

        if(requireActivity().intent.getBooleanExtra(IS_HIDE_DATA, false)){
            binding.cancelBtn.toGone()
            binding.acceptBtn.toGone()
        }

        binding.cancelBtn.setOnClickListener {
            recipesListViewModel.updateOrderStatus(
                UpdateOrderStatus(
                    SharedPreferencesUtils.getIntPreference(
                        SharedPreferencesUtils.PREF_USER_ID
                    ), orderInfo!!.number, "Cancelled"
                )
            )
        }

        binding.acceptBtn.setOnClickListener {
            recipesListViewModel.updateOrderStatus(
                UpdateOrderStatus(
                    SharedPreferencesUtils.getIntPreference(
                        SharedPreferencesUtils.PREF_USER_ID
                    ), orderInfo!!.number, "Finished"
                )
            )
        }

        binding.addressNavigation.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+binding.address!!.address.address_line1+binding.address!!.address.address_line2+binding.address!!.address.city+binding.address!!.address.state+binding.address!!.address.zip)
            )
            startActivity(intent)
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


    private fun bindListData(recipes: ScheduleOrders) {
        if (!(recipes.items.isNullOrEmpty())) {
            recipesAdapter = OrderinfoAdapter(recipesListViewModel, recipes.items)
            binding.cartItemsRecyclerView.adapter = recipesAdapter
            binding.address = recipes
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
}