package com.jagadish.freshmart.view.main.ui.store

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.CATEGORY_KEY
import com.jagadish.freshmart.R
import com.jagadish.freshmart.RESULT_ACTIVITY_IS_VIEW_CART
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.shop.Shop
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentHomeBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.main.MainActivity
import com.jagadish.freshmart.view.main.ui.store.adapter.HomeAdapter
import com.jagadish.freshmart.view.main.ui.store.model.HomeModel
import com.jagadish.freshmart.view.products.ProductsListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class StoreFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private val recipesListViewModel: StoreViewModel by viewModels()
    private lateinit var recipesAdapter: HomeAdapter
    private val REQUEST_CATEGORY_VIEW_CART = 214

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        val layoutManager = LinearLayoutManager(context)
        binding.storeRecyclerView.layoutManager = layoutManager
        binding.storeRecyclerView.setHasFixedSize(true)
        recipesListViewModel.getRecipes()
    }
    //https://jsonblob.com/eccd16e6-7a2e-11eb-becc-a3dfce0ac389
    //https://jsonblob.com/api/jsonBlob/eccd16e6-7a2e-11eb-becc-a3dfce0ac389

    private fun observeViewModel() {
        (activity as MainActivity).currentAddress.observe(viewLifecycleOwner, Observer {
            featureName.text = it.featureName
            address_txt.text = it.getAddressLine(0)
        })
        observe(recipesListViewModel.recipesLiveData, ::handleRecipesList)
        observe(recipesListViewModel.recipeSearchFound, ::showSearchResult)
        observe(recipesListViewModel.noSearchFound, ::noSearchResult)
        observeEvent(recipesListViewModel.openRecipeDetails, ::navigateToDetailsScreen)
        observeSnackBarMessages(recipesListViewModel.showSnackBar)
        observeToast(recipesListViewModel.showToast)

    }


    private fun handleSearch(query: String) {
        if (query.isNotEmpty()) {
            binding.pbLoading.visibility = View.VISIBLE
        }
    }


    private fun bindListData(recipes: Shop) {
        if (!(recipes.shopList.isNullOrEmpty())) {
            val list = ArrayList<HomeModel>()
            for (i in 1..2) {
                when (i) {
                    1 -> list.add(
                        HomeModel(
                            recipes.banners as ArrayList<ShopItem>,
                            recipes.shopList as ArrayList<ShopItem>
                        )
                    )
                    2 -> list.add(
                        HomeModel(
                            recipes.banners as ArrayList<ShopItem>,
                            recipes.shopList as ArrayList<ShopItem>
                        )
                    )
                }
            }
            recipesAdapter = HomeAdapter(requireActivity(), list, recipesListViewModel)
            binding.storeRecyclerView.adapter = recipesAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<ShopItem>) {
        navigateEvent.getContentIfNotHandled()?.let {
            val nextScreenIntent =
                Intent(requireActivity(), ProductsListActivity::class.java).apply {
                    putExtra(CATEGORY_KEY, it)
                }
            startActivityForResult(nextScreenIntent, REQUEST_CATEGORY_VIEW_CART)
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
        binding.storeRecyclerView.visibility = if (show) View.VISIBLE else View.GONE
        binding.pbLoading.toGone()
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.storeRecyclerView.toGone()
    }


    private fun showSearchResult(recipesItem: ShopItem) {
        recipesListViewModel.openRecipeDetails(recipesItem)
        binding.pbLoading.toGone()
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()
        binding.pbLoading.toGone()
    }

    private fun handleRecipesList(status: Resource<Shop>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(recipes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { recipesListViewModel.showToastMessage(it) }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CATEGORY_VIEW_CART) {
            if (resultCode == AppCompatActivity.RESULT_OK && data!!.getBooleanExtra(
                    RESULT_ACTIVITY_IS_VIEW_CART, false
                )
            )
                findNavController().navigate(R.id.action_navigation_store_to_navigation_cart)
        }
    }
}