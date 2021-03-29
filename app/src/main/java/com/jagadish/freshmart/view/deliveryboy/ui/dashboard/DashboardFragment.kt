package com.jagadish.freshmart.view.deliveryboy.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.*
import com.jagadish.freshmart.RESULT_ACTIVITY_IS_VIEW_CART
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
import com.jagadish.freshmart.view.main.MainActivity
import com.jagadish.freshmart.view.main.ui.cart.adapter.CartItemsAdapter
import com.jagadish.freshmart.view.main.ui.store.model.SelectedAddress
import com.jagadish.freshmart.view.orderinfo.OrderInfoActivity
import com.jagadish.freshmart.view.products.ProductsListActivity
import com.oneclickaway.opensource.placeautocomplete.data.api.bean.place_details.PlaceDetails
import com.oneclickaway.opensource.placeautocomplete.utils.SearchPlacesStatusCodes
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
    if(requireActivity().intent.getBooleanExtra(Is_INTERNAL_DETAILS,false)){
      findNavController().navigate(R.id.action_order_list_to_order_info)
    }else if(requireActivity().intent.getBooleanExtra(IS_COME_PROFILE,false)) {
      binding.titleHeader.toGone()
      dashboardViewModel.fetchAllOrders()
    }else {
      dashboardViewModel.fetchScheduleOrders()
    }
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
          putExtra(Is_INTERNAL_DETAILS, true)
          if(requireActivity().intent.getBooleanExtra(IS_COME_PROFILE,false)){
            putExtra(IS_HIDE_DATA, true)
          }

        }
      startActivityForResult(nextScreenIntent, 901)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 901) {
      if (resultCode == AppCompatActivity.RESULT_OK && data!!.getBooleanExtra(
          RESULT_ACTIVITY_ORDER_STATUS, false
        )
      )
        dashboardViewModel.fetchScheduleOrders()
    }
  }
}