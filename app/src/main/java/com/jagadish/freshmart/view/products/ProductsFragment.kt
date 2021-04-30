package com.jagadish.freshmart.view.products

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.CATEGORY_KEY
import com.jagadish.freshmart.RESULT_ACTIVITY_IS_VIEW_CART
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.dto.cart.AddItemRes
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentProductsBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.details.ProductDetailsActivity
import com.jagadish.freshmart.view.products.adapter.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass as the defaultAddress destination in the navigation.
 */
@AndroidEntryPoint
class ProductsFragment : BaseFragment() {

    private lateinit var binding: FragmentProductsBinding
    private val recipesListViewModel: ProductsFragmentViewModel by viewModels()
    private lateinit var recipesAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

        observeViewModel()
        val layoutManager = LinearLayoutManager(context)
        binding.productsRecyclerView.layoutManager = layoutManager
        binding.productsRecyclerView.setHasFixedSize(true)
        val itemDecor = DividerItemDecoration(context, VERTICAL)
        binding.productsRecyclerView.addItemDecoration(itemDecor)
        binding.viewCart.setOnClickListener { val data = Intent().apply {
            putExtra(RESULT_ACTIVITY_IS_VIEW_CART, true)
        }
            requireActivity().setResult(RESULT_OK, data)
            requireActivity(). finish() }

        binding.searchProducts.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                recipesListViewModel.searchProducts(s.toString())
            }

        })
    }

    override fun onResume() {
        super.onResume()
        binding.viewCartLayout.toGone()
        val category = requireActivity().intent.getParcelableExtra<ShopItem>(CATEGORY_KEY)
        recipesListViewModel.getRecipes(category!!.id)
    }

    private fun observeViewModel() {
        observeEvent(recipesListViewModel.openRecipeDetails, ::navigateToDetailsScreen)
        observeEvent(recipesListViewModel.openCartView, ::showCartView)
        observe(recipesListViewModel.recipesLiveData, ::handleRecipesList)
        observe(recipesListViewModel.recipeSearchFound, ::showSearchResult)
        observe(recipesListViewModel.noSearchFound, ::noSearchResult)
        observe(recipesListViewModel.removeCartItem, ::removeCarItemSuccess)
        observeSnackBarMessages(recipesListViewModel.showSnackBar)
        observeToast(recipesListViewModel.showToast)

    }


    private fun handleSearch(query: String) {
        if (query.isNotEmpty()) {
            binding.pbLoading.visibility = View.VISIBLE
        }
    }


    private fun bindListData(recipes: Products) {
        if (!(recipes.products.isNullOrEmpty())) {
            if(Singleton.getInstance().cart != null && Singleton.getInstance().cart!!.products.isNotEmpty()){
                var count = 0
                for(item in Singleton.getInstance().cart.products){
                    for(product in recipes.products)
                    if(item.id == product.id){
                        count++
                        product.quantity = item.quantity
                        product.isAddCart = true
                    }
                }
                binding.viewCartLayout.toVisible()
                binding.priceDetails = AddItemRes(true,"",Singleton.getInstance().cart.products.size,0.0,recipes.products)
//                recipesListViewModel.checkCartItems(AddItemRes(true,"",Singleton.getInstance().cart.count,Singleton.getInstance().cart.total_price,ArrayList()))
            }else{
                binding.viewCartLayout.toGone()
            }

            recipesAdapter = ProductsAdapter(recipesListViewModel, recipes.products)
            binding.productsRecyclerView.adapter = recipesAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<ProductsItem>) {
        navigateEvent.getContentIfNotHandled()?.let {
            val nextScreenIntent = Intent(requireActivity(), ProductDetailsActivity::class.java).apply {
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
        showDataView(false)
//        recipesListViewModel.showToastMessage(SEARCH_ERROR)
    }

    private fun showDataView(show: Boolean) {
        binding.tvNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.productsRecyclerView.visibility = if (show) View.VISIBLE else View.GONE
        binding.pbLoading.toGone()
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.productsRecyclerView.toGone()
    }


    private fun showSearchResult(recipesItem: MutableList<ProductsItem>) {
//        recipesListViewModel.openRecipeDetails(recipesItem)
//        binding.pbLoading.toGone()
        if(recipesItem.size >0) {
            recipesAdapter.setFitersItems(recipesItem)
        }else{
            recipesAdapter.showAllItems()
        }
        showDataView(true)
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()
        binding.pbLoading.toGone()
    }

    private fun handleRecipesList(status: Resource<Products>) {
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
            if(navigateEvent.peekContent().count>0) {
                binding.viewCartLayout.toVisible()
                binding.priceDetails = navigateEvent.peekContent()
            }else{
                binding.viewCartLayout.toGone()
                binding.priceDetails = navigateEvent.peekContent()
            }
        }
    }

    private fun removeCarItemSuccess(productsItem: SingleEvent<ProductsItem>) {
        recipesAdapter.getItems()[recipesAdapter.getItems().indexOf(productsItem.peekContent())].isLoad = false
        recipesAdapter.notifyItemChanged(recipesAdapter.getItems().indexOf(productsItem.peekContent()))

    }
}