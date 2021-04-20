package com.jagadish.freshmart.view.main.ui.store

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.BuildConfig
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
import com.jagadish.freshmart.view.main.ui.store.adapter.BannersAdapter
import com.jagadish.freshmart.view.main.ui.store.adapter.HomeAdapter
import com.jagadish.freshmart.view.main.ui.store.adapter.StoreAdapter
import com.jagadish.freshmart.view.main.ui.store.model.HomeModel
import com.jagadish.freshmart.view.main.ui.store.model.SelectedAddress
import com.jagadish.freshmart.view.products.ProductsListActivity
import com.oneclickaway.opensource.placeautocomplete.data.api.bean.place_details.PlaceDetails
import com.oneclickaway.opensource.placeautocomplete.ui.SearchPlaceActivity
import com.oneclickaway.opensource.placeautocomplete.utils.SearchPlacesStatusCodes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList

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
        val window = requireActivity()!!.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            com.jagadish.freshmart.R.color.white
        )
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        val layoutManager = LinearLayoutManager(context)
        binding.storeRecyclerView.layoutManager = layoutManager
        binding.storeRecyclerView.setHasFixedSize(true)
        val layoutManagerSearch = LinearLayoutManager(context)
        binding.searchRecyclerview.layoutManager = layoutManagerSearch
        binding.searchRecyclerview.setHasFixedSize(true)

        binding.searchHome.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                recipesListViewModel.searchProducts(s.toString())
            }

        })

        binding.searchClear.setOnClickListener {
            binding.searchHome.text?.clear()
        }

        binding.addressLayout.setOnClickListener {
            val intent = Intent(requireContext(), SearchPlaceActivity::class.java)
            intent.putExtra(
                SearchPlacesStatusCodes.CONFIG,
                SearchPlaceActivity.Config.Builder(apiKey = BuildConfig.ApiKey)
                    .setSearchBarTitle("Enter Source Location")
                    .setMyLocation("12.9716,77.5946")
                    .setEnclosingRadius("500")
                    .build()

            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val pair = Pair.create(
                    (binding.addressTxt as? View),
                    SearchPlacesStatusCodes.PLACEHOLDER_TRANSITION
                )
                val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), pair).toBundle()
                startActivityForResult(intent, 700, options)

            } else {
                startActivityForResult(intent, 700)
//                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }
        }

    }
    //https://jsonblob.com/eccd16e6-7a2e-11eb-becc-a3dfce0ac389
    //https://jsonblob.com/api/jsonBlob/eccd16e6-7a2e-11eb-becc-a3dfce0ac389

    private fun observeViewModel() {
        (activity as MainActivity).currentAddress.observe(viewLifecycleOwner, Observer {
            featureName.text = it.shortAddress
            address_txt.text = it.longAddress
            recipesListViewModel.getRecipes(it.postalCode)
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
        binding.storeRecyclerView.toVisible()
        binding.searchRecyclerview.toGone()
        binding.searchClear.toGone()
//        binding.searchRecyclerview.hideKeyboard()
//        recipesListViewModel.showToastMessage(SEARCH_ERROR)
    }

    private fun showDataView(show: Boolean) {
        binding.searchNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.storeRecyclerView.visibility = if (show) View.VISIBLE else View.GONE
        binding.searchHome.visibility = if (show) View.VISIBLE else View.GONE
        binding.pbLoading.toGone()
    }
    private fun showErrorLocationDataView(show: Boolean) {
        binding.locationNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.storeRecyclerView.visibility = if (show) View.VISIBLE else View.GONE
        binding.searchHome.visibility = if (show) View.VISIBLE else View.GONE
        binding.pbLoading.toGone()
    }
    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.searchNoData.toGone()
        binding.locationNoData.toGone()
        binding.storeRecyclerView.toGone()
    }


    private fun showSearchResult(recipesItem: List<ShopItem>) {
        binding.storeRecyclerView.toGone()
        binding.searchRecyclerview.toVisible()
        binding.searchClear.toVisible()
        binding.searchRecyclerview.adapter = StoreAdapter(recipesListViewModel, recipesItem)

//        recipesListViewModel.openRecipeDetails(recipesItem)
//        binding.pbLoading.toGone()
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()
        binding.pbLoading.toGone()
    }

    private fun handleRecipesList(status: Resource<Shop>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let {
            if(it.success) {
                bindListData(recipes = it)
            }else {
                showErrorLocationDataView(false)
            }
            }
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
        }else if (requestCode == 700 && resultCode == Activity.RESULT_OK) {

            val placeDetails =
                data?.getParcelableExtra<PlaceDetails>(SearchPlacesStatusCodes.PLACE_DATA)
            val mGeocoder = Geocoder(requireActivity(), Locale.getDefault())
            var addresses = placeDetails!!.geometry!!.location!!.lat?.let {
                placeDetails!!.geometry!!.location!!.lng?.let { it1 ->
                    mGeocoder.getFromLocation(
                        it,
                        it1,
                        1
                    )
                }
            }
//            var postalCode = ""
//            for( item in placeDetails!!.addressComponents!!){
//                if(item?.types?.contains("postal_code") == true){
//                    postalCode = item.longName
//                }
//            }
            var address =""
             if(addresses?.get(0)?.subLocality != null){
                 address =  addresses.get(0)!!.subLocality
             } else{
                 address = addresses?.get(0)!!.featureName
             }
            (activity as MainActivity).address.value = SelectedAddress(address ,addresses[0].getAddressLine(0),addresses[0].postalCode)

//                placeDetails.name?.let { placeDetails.formattedAddress?.let { it1 ->
//                    SelectedAddress(it,
//                        it1,postalCode)
//                } }
        }
    }
}