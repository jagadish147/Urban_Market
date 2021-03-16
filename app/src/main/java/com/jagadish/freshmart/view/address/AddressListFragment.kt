package com.jagadish.freshmart.view.address

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
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.address.AddressRes
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentAddressListBinding
import com.jagadish.freshmart.databinding.FragmentLoginBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.address.adapter.AddressAdapter
import com.jagadish.freshmart.view.products.ProductsListActivity
import com.jagadish.freshmart.view.products.adapter.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AddressListFragment : BaseFragment() {

    private lateinit var binding: FragmentAddressListBinding
    private val addessViewModel: AdressViewModel by viewModels()
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        val layoutManager = LinearLayoutManager(context)
        binding.addressRecyclerView.layoutManager = layoutManager
        binding.addressRecyclerView.setHasFixedSize(true)
        addessViewModel.fetchAddress()
        binding.addNewAddress.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_address_list_to_navigation_address_add)
        }
    }

    private fun observeViewModel() {
        observe(addessViewModel.addressLiveData, ::handleRecipesList)
        observe(addessViewModel.noSearchFound, ::noSearchResult)
        observeSnackBarMessages(addessViewModel.showSnackBar)
        observeToast(addessViewModel.showToast)

    }


    private fun handleSearch(query: String) {
        if (query.isNotEmpty()) {
            binding.pbLoading.visibility = View.VISIBLE
        }
    }


    private fun bindListData(recipes: AddressRes) {
        if (!(recipes.addresses.isNullOrEmpty())) {
            addressAdapter = AddressAdapter(addessViewModel,recipes.addresses )
            binding.addressRecyclerView.adapter = addressAdapter
            showDataView(true)
        } else {
            showDataView(false)
            findNavController().navigate(R.id.action_navigation_address_list_to_navigation_address_add)
        }
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<ProductsItem>) {
        navigateEvent.getContentIfNotHandled()?.let {
            val nextScreenIntent = Intent(requireActivity(), ProductsListActivity::class.java).apply {
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
        addessViewModel.showToastMessage(SEARCH_ERROR)
    }

    private fun showDataView(show: Boolean) {
        binding.tvNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.addressRecyclerView.visibility = if (show) View.VISIBLE else View.GONE
        binding.pbLoading.toGone()
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.addressRecyclerView.toGone()
    }


    private fun showSearchResult(recipesItem: AddAddressReq) {
        addessViewModel.openRecipeDetails(recipesItem)
        binding.pbLoading.toGone()
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()
        binding.pbLoading.toGone()
    }

    private fun handleRecipesList(status: Resource<AddressRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(recipes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { addessViewModel.showToastMessage(it) }
            }
        }
    }

}