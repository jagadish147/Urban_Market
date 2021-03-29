package com.jagadish.freshmart.view.main.ui.cart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.CATEGORY_KEY
import com.jagadish.freshmart.R
import com.jagadish.freshmart.RESULT_ACTIVITY_DEFAULT_ADDRESS
import com.jagadish.freshmart.RESULT_ACTIVITY_DEFAULT_ADDRESS_DATA
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.address.AddressRes
import com.jagadish.freshmart.data.dto.cart.AddItemRes
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.order.OrderRes
import com.jagadish.freshmart.data.dto.order.PaymentStatusReq
import com.jagadish.freshmart.data.dto.order.PaymentStatusRes
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentCartBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.address.AddressActivity
import com.jagadish.freshmart.view.login.LoginActivity
import com.jagadish.freshmart.view.main.ui.cart.adapter.CartItemsAdapter
import com.jagadish.freshmart.view.orderinfo.OrderInfoActivity
import com.jagadish.freshmart.view.payment.status.OrderStatusActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartFragment : BaseFragment() {

    private lateinit var binding: FragmentCartBinding
    private val recipesListViewModel: CartViewModel by viewModels()
    private lateinit var recipesAdapter: CartItemsAdapter

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

        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

        observeViewModel()
        val layoutManager = LinearLayoutManager(context)
        binding.cartItemsRecyclerView.layoutManager = layoutManager
        binding.cartItemsRecyclerView.setHasFixedSize(true)
        recipesListViewModel.getRecipes()


        binding.orderConfirmBtn.setOnClickListener {

                if (SharedPreferencesUtils.getBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN)) {
                    if (binding.address != null) {
                        recipesListViewModel.createOrderId(binding.address!!.id)
                    } else {
                        Validator.setError(
                            binding.orderConfirmBtn,
                            resources.getString(R.string.error_address)
                        )
                    }
                } else {
                    val nextScreenIntent =
                        Intent(requireActivity(), LoginActivity::class.java).apply {
//                putExtra(CATEGORY_KEY, it)
                        }
                    startActivity(nextScreenIntent)
                }

        }

        binding.addAddressBtn.setOnClickListener {
            if (SharedPreferencesUtils.getBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN)) {
                val nextScreenIntent =
                    Intent(requireActivity(), AddressActivity::class.java).apply {
//                putExtra(CATEGORY_KEY, it)
                    }
                startActivityForResult(nextScreenIntent, 202)
            }else{
                val nextScreenIntent =
                    Intent(requireActivity(), LoginActivity::class.java).apply {
//                putExtra(CATEGORY_KEY, it)
                    }
                startActivity(nextScreenIntent)
            }
        }
    }


    private fun observeViewModel() {
        observe(recipesListViewModel.recipesLiveData, ::handleRecipesList)
        observe(recipesListViewModel.recipeSearchFound, ::showSearchResult)
        observe(recipesListViewModel.noSearchFound, ::noSearchResult)
        observeEvent(recipesListViewModel.openRecipeDetails, ::navigateToDetailsScreen)
        observeEvent(recipesListViewModel.openCartView, ::showCartView)
        observeSnackBarMessages(recipesListViewModel.showSnackBar)
        observeToast(recipesListViewModel.showToast)
        observe(recipesListViewModel.addressLiveData, ::handleAddressList)
        observe(recipesListViewModel.orderIdLiveData, ::handleOrderId)
        observe(recipesListViewModel.paymentStatusLiveData, ::handlePaymentStatus)
    }


    private fun handleSearch(query: String) {
        if (query.isNotEmpty()) {
            binding.pbLoading.visibility = View.VISIBLE
        }
    }


    private fun bindListData(recipes: Cart) {
        if (!(recipes.products.isNullOrEmpty())) {
            if(SharedPreferencesUtils.getBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN))
                recipesListViewModel.fetchAddress()
            recipesAdapter = CartItemsAdapter(recipesListViewModel, recipes.products)
            binding.cartItemsRecyclerView.adapter = recipesAdapter
            binding.cart = recipes
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
            val cart = binding.cart
            cart!!.order_price = it.total_price
            cart!!.total_price = it.total_price
            binding.cart = cart
            binding.orderInfoLayout.toVisible()
        }
    }

    private fun handleAddressList(status: Resource<AddressRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindAddressListData(recipes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { recipesListViewModel.showToastMessage(it) }
            }
        }
    }

    private fun bindAddressListData(recipes: AddressRes) {
        if (!(recipes.addresses.isNullOrEmpty())) {
            for(item in recipes.addresses){
                if(item.defaultAddress){
                    binding.addAddressBtn.text = "Change Address"
                    binding.defaultAddress.text = item.address_line1 +","+ item.address_line2 +","+item.city
                    binding.address = item
                    break
                }
            }
            showDataView(true)
        } else {
            showDataView(false)
            binding.addAddressBtn.text = "Add Address"
        }
    }

    private fun handleOrderId(status: Resource<OrderRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindOrderId(orderRes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { recipesListViewModel.showToastMessage(it) }
            }
        }
    }

    private fun bindOrderId(orderRes: OrderRes){
        val paymentstatusReq = PaymentStatusReq(orderRes.order_id, "Card", "123456")
        recipesListViewModel.checkPaymentStatus(paymentstatusReq)
    }

    private fun handlePaymentStatus(status: Resource<PaymentStatusRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindPaymentStatus(paymentStatusRes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { recipesListViewModel.showToastMessage(it) }
            }
        }
    }

    private fun bindPaymentStatus(paymentStatusRes: PaymentStatusRes){
            if(paymentStatusRes.success && paymentStatusRes.status == 200){
                startActivity(Intent(requireActivity(), OrderStatusActivity::class.java))
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 202 && resultCode == Activity.RESULT_OK) {
            if(data?.getBooleanExtra(RESULT_ACTIVITY_DEFAULT_ADDRESS, false)!!){
                val addressReq = data.getParcelableExtra<AddAddressReq>(
                    RESULT_ACTIVITY_DEFAULT_ADDRESS_DATA
                )
                binding.addAddressBtn.text = "Change Address"
                binding.defaultAddress.text = addressReq!!.address_line1 +","+ addressReq.address_line2 +","+addressReq.city
                binding.address = addressReq
            }
        }
    }
}