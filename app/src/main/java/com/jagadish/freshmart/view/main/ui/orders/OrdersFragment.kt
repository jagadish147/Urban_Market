package com.jagadish.freshmart.view.main.ui.orders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.order.OrderItems
import com.jagadish.freshmart.data.dto.order.OrdersRes
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentCartBinding
import com.jagadish.freshmart.databinding.FragmentOrdersBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.address.AddressActivity
import com.jagadish.freshmart.view.address.AddressListFragmentDirections
import com.jagadish.freshmart.view.login.LoginActivity
import com.jagadish.freshmart.view.main.ui.cart.CartViewModel
import com.jagadish.freshmart.view.main.ui.cart.adapter.CartItemsAdapter
import com.jagadish.freshmart.view.main.ui.orders.adapter.OrdersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment() {

    private lateinit var binding: FragmentOrdersBinding
    private val recipesListViewModel: OrdersViewModel by viewModels()
    private lateinit var recipesAdapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val window = requireActivity()!!.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            com.jagadish.freshmart.R.color.main_color
        )
        binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        val layoutManager = LinearLayoutManager(context)
        binding.ordersRecyclerView.layoutManager = layoutManager
        binding.ordersRecyclerView.setHasFixedSize(true)
        val itemDecor = DividerItemDecoration(context, LinearLayout.VERTICAL)
        binding.ordersRecyclerView.addItemDecoration(itemDecor)
        recipesListViewModel.getOrders()


    }

    private fun observeViewModel() {
        observe(recipesListViewModel.recipesLiveData, ::handleRecipesList)
        observe(recipesListViewModel.orderItemClick, ::handleOnClickRecipesList)
        observeSnackBarMessages(recipesListViewModel.showSnackBar)
        observeToast(recipesListViewModel.showToast)

    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }


    private fun showDataView(show: Boolean) {
        binding.tvNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.ordersRecyclerView.visibility = if (show) View.VISIBLE else View.GONE
        binding.pbLoading.toGone()
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.ordersRecyclerView.toGone()
    }


    private fun handleRecipesList(status: Resource<OrdersRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(recipes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { recipesListViewModel.showToastMessage(it) }
            }
        }
    }

    private fun bindListData(recipes: OrdersRes) {
        if (!(recipes.orders.isNullOrEmpty())) {
            recipesAdapter = OrdersAdapter(recipesListViewModel,recipes.orders )
            binding.ordersRecyclerView.adapter = recipesAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun handleOnClickRecipesList(orderItem: SingleEvent<OrderItems>) {

        val bundle = OrdersFragmentDirections.actionNavigationOrdersToNavigationOrderSummary(
            orderItem.peekContent()
        )
        findNavController().navigate(bundle)
    }
}