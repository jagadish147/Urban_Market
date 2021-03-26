package com.jagadish.freshmart.view.deliveryboy.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.CATEGORY_KEY
import com.jagadish.freshmart.ORDER_INFO
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentDashboardBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.deliveryboy.ui.dashboard.adapter.ScheduleOrdersAdapter
import com.jagadish.freshmart.view.main.ui.cart.adapter.CartItemsAdapter
import com.jagadish.freshmart.view.orderinfo.OrderInfoActivity
import com.jagadish.freshmart.view.products.ProductsListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {

  private lateinit var binding : FragmentDashboardBinding
  private  val dashboardViewModel: DashboardViewModel by viewModels()
  private lateinit var scheduleOrdersAdapter: ScheduleOrdersAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentDashboardBinding.inflate(inflater,container,false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    observeViewModel()
    val layoutManager = LinearLayoutManager(context)
    binding.cartItemsRecyclerView.layoutManager = layoutManager
    binding.cartItemsRecyclerView.setHasFixedSize(true)
    dashboardViewModel.fetchScheduleOrders()
  }

  private fun observeViewModel() {
    observe(dashboardViewModel.addressLiveData, ::handleRecipesList)
    observe(dashboardViewModel.recipeSearchFound, ::showSearchResult)
    observe(dashboardViewModel.noSearchFound, ::noSearchResult)
    observeEvent(dashboardViewModel.onItemClickLiveData, ::onNavigateOrderInfo)
    observeSnackBarMessages(dashboardViewModel.showSnackBar)
    observeToast(dashboardViewModel.showToast)
  }
  private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
    binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
  }

  private fun observeToast(event: LiveData<SingleEvent<Any>>) {
    binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
  }

  private fun showSearchResult(recipesItem: DeliveryBoyOrders) {
    dashboardViewModel.openRecipeDetails(recipesItem)
    binding.pbLoading.toGone()
  }

  private fun noSearchResult(unit: Unit) {
    showSearchError()
    binding.pbLoading.toGone()
  }
  private fun showSearchError() {
    dashboardViewModel.showToastMessage(SEARCH_ERROR)
  }

  private fun handleRecipesList(status: Resource<DeliveryBoyOrders>) {
    when (status) {
      is Resource.Loading -> showLoadingView()
      is Resource.Success -> status.data?.let { bindListData(recipes = it) }
      is Resource.DataError -> {
        showDataView(false)
        status.errorCode?.let { dashboardViewModel.showToastMessage(it) }
      }
    }
  }

  private fun showLoadingView() {
    binding.pbLoading.toVisible()
    binding.tvNoData.toGone()
    binding.cartItemsRecyclerView.toGone()
  }

  private fun bindListData(recipes: DeliveryBoyOrders) {
    if (!(recipes.orders.isNullOrEmpty())) {
      scheduleOrdersAdapter = ScheduleOrdersAdapter(dashboardViewModel,recipes.orders )
      binding.cartItemsRecyclerView.adapter = scheduleOrdersAdapter
      showDataView(true)
    } else {
      showDataView(false)
    }
  }

  private fun showDataView(show: Boolean) {
    binding.tvNoData.visibility = if (show) View.GONE else View.VISIBLE
    binding.cartItemsRecyclerView.visibility = if (show) View.VISIBLE else View.GONE
    binding.pbLoading.toGone()
  }

  private fun onNavigateOrderInfo(scheduleOrders: SingleEvent<ScheduleOrders>){
    scheduleOrders.getContentIfNotHandled()?.let {
      val nextScreenIntent =
        Intent(requireActivity(), OrderInfoActivity::class.java).apply {
          putExtra(ORDER_INFO, it)
        }
      startActivity(nextScreenIntent)
    }
  }
}