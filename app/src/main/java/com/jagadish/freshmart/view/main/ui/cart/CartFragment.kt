package com.jagadish.freshmart.view.main.ui.cart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.*
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.address.AddressRes
import com.jagadish.freshmart.data.dto.cart.AddItemRes
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.order.OrderRes
import com.jagadish.freshmart.data.dto.order.PaymentStatusRes
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentCartBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.address.AddressActivity
import com.jagadish.freshmart.view.login.LoginActivity
import com.jagadish.freshmart.view.main.MainActivity
import com.jagadish.freshmart.view.main.ui.cart.adapter.CartItemsAdapter
import com.jagadish.freshmart.view.orderinfo.OrderInfoActivity
import com.jagadish.freshmart.view.payment.OrderPlaceActivity
import com.jagadish.freshmart.view.payment.PaymentDetailsModel
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
        (binding.cartItemsRecyclerView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false

        val itemDecor = DividerItemDecoration(context, LinearLayout.VERTICAL)
        binding.cartItemsRecyclerView.addItemDecoration(itemDecor)
        recipesListViewModel.getRecipes()


        binding.orderConfirmBtn.setOnClickListener {

                if (SharedPreferencesUtils.getBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN)) {
                    if (binding.address != null) {
                        val nextScreenIntent =
                            Intent(requireActivity(), OrderPlaceActivity::class.java).apply {
                                putExtra(
                                    PAYMENT_DETAILS, PaymentDetailsModel(
                                        SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_NAME),
                                        SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_MOBILE),
                                        binding.cart!!.count,
                                        binding.address!!,
                                        binding.cart!!.total_price,
                                        binding.cart!!.discount_price,
                                        binding.cart!!.delivery_charge,
                                        (binding.cart!!.total_price+binding.cart!!.delivery_charge),
                                        OrderRes()
                                    )
                                )
                            }
                        startActivity(nextScreenIntent)
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
                        putExtra(IS_COME_CHANGE_ADDRESS, true)
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
        observe(recipesListViewModel.paymentStatusLiveData, ::handlePaymentStatus)
        observe(recipesListViewModel.updatePaymentViewItem, ::updatePaymentViewSuccess)
        observe(recipesListViewModel.removeCartItem, ::removeCarItemSuccess)
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
            recipesAdapter = CartItemsAdapter(recipesListViewModel, ArrayList(recipes.products))
            binding.cartItemsRecyclerView.adapter = recipesAdapter
            binding.cart = recipes
            Singleton.getInstance().cart = recipes
            (activity as MainActivity).updateCart()
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
//        binding.cartItemsRecyclerView.toGone()
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

            if(it.success && it.count>0) {
                val cart = binding.cart
                cart!!.order_price = it.total_price
                cart!!.total_price = it.total_price
                cart!!.count = it.count
                cart!!.products = it.items
                cart!!.delivery_charge = it.delivery_charge
                cart!!.discount_price = it.discount_price

                binding.cart = Cart(
                    it.success,
                    it.message,
                    it.items,
                    it.total_price,
                    it.delivery_charge,
                    it.count,
                    cart.min_delivery,
                    it.discount_price
                )
                recipesAdapter.updateItems(ArrayList(it.items)) // = CartItemsAdapter(recipesListViewModel, ArrayList(it.items))
//                binding.cartItemsRecyclerView.adapter = recipesAdapter
//                recipesAdapter.notifyDataSetChanged()
                Singleton.getInstance().cart = cart
                (activity as MainActivity).updateCart()
                binding.orderInfoLayout.toVisible()
            }else{
                recipesAdapter = CartItemsAdapter(recipesListViewModel, ArrayList(it.items))
                binding.cartItemsRecyclerView.adapter = recipesAdapter
                binding.orderInfoLayout.toGone()
                showDataView(false)
                Singleton.getInstance().cart = Cart()
                (activity as MainActivity).updateCart()
            }
        }
    }

    private fun updateCartView(productItem: SingleEvent<ProductsItem>) {
        recipesAdapter.getItems()[recipesAdapter.getItems().indexOf(productItem.peekContent())].price = productItem.peekContent().price
        recipesAdapter.getItems()[recipesAdapter.getItems().indexOf(productItem.peekContent())].discount_price = productItem.peekContent().discount_price
        recipesAdapter.notifyItemChanged(recipesAdapter.getItems().indexOf(productItem.peekContent()))
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
                    binding.defaultAddress.visibility = View.VISIBLE
                    binding.address = item
                    break
                }
            }
            showDataView(true)
        } else {
            showDataView(true)
            binding.addAddressBtn.text = "Add Address"
        }
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
                if(SharedPreferencesUtils.getBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN))
                    recipesListViewModel.fetchAddress()
//                val addressReq = data.getParcelableExtra<AddAddressReq>(
//                    RESULT_ACTIVITY_DEFAULT_ADDRESS_DATA
//                )
//                binding.addAddressBtn.text = "Change Address"
//                binding.defaultAddress.text = addressReq!!.address_line1 +","+ addressReq.address_line2 +","+addressReq.city
//                binding.defaultAddress.visibility = View.VISIBLE
//                binding.address = addressReq
            }
        }
    }

    private fun updatePaymentViewSuccess(productsItem: SingleEvent<Int>) {
        if(productsItem.peekContent() == 0){
            binding.orderInfoLayout.toGone()
            showDataView(false)
            Singleton.getInstance().cart = Cart()
            (activity as MainActivity).updateCart()
        }else{

        }
    }

    private fun removeCarItemSuccess(productsItem: SingleEvent<ProductsItem>) {
        if (recipesAdapter.getItems()[recipesAdapter.getItems().indexOf(productsItem.peekContent())].quantity == 0) {
            recipesAdapter.getItems()[recipesAdapter.getItems().indexOf(productsItem.peekContent())].isAddCart = false
            recipesAdapter.getItems()[recipesAdapter.getItems().indexOf(productsItem.peekContent())].quantity = 0
            recipesAdapter.getItems()[recipesAdapter.getItems().indexOf(productsItem.peekContent())].isLoad = true
            recipesAdapter.getItems().remove(productsItem.peekContent())
            recipesAdapter.notifyDataSetChanged()
            recipesListViewModel.checkCartItemsSze(recipesAdapter.getItems().size)
        }else {
            recipesAdapter.getItems()[recipesAdapter.getItems()
                .indexOf(productsItem.peekContent())].isLoad = false
            recipesAdapter.notifyItemChanged(
                recipesAdapter.getItems().indexOf(productsItem.peekContent())
            )
        }
    }
}