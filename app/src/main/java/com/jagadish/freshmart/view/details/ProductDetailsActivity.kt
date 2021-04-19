package com.jagadish.freshmart.view.details

import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.CATEGORY_KEY
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.AddItemReq
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ActivityProductDetailsBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.products.ProductsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private val recipesListViewModel: ProductDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_details)

        setSupportActionBar(findViewById(R.id.toolbar))


        supportActionBar!!.title = "Details"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        observeViewModel()
        val productDetails = intent.getParcelableExtra<ProductsItem>(CATEGORY_KEY)

        binding.productName.text = productDetails!!.name
        if(productDetails.quantity != 0)
            binding.productPrice.text = "₹ ${productDetails.price- (productDetails.discount_price* productDetails.quantity)}"
        else
            binding.productPrice.text = "₹ ${productDetails.price- (productDetails.discount_price)}"

        if(productDetails.discount_price != 0.00) {
            binding.discountPrice.text = (productDetails.price).toString()
            binding.discountPrice.paintFlags =
                (binding.discountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
        }

        Glide.with(this).load(productDetails!!.image.url).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).diskCacheStrategy(
            DiskCacheStrategy.DATA).transform(CenterInside(), RoundedCorners(24)).into(binding.productImageDetails)


        binding.description.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(productDetails.description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(productDetails.description)
        }

        if(productDetails.isAddCart){
            binding.quantityLayout.visibility = View.VISIBLE
            binding.addBtn.visibility = View.GONE
            binding.quantityTxt.text = productDetails.quantity.toString()
        }else{
            binding.quantityLayout.visibility = View.GONE
            binding.addBtn.visibility = View.VISIBLE
            binding.quantityTxt.text = productDetails.quantity.toString()
        }


        binding.addBtn.setOnClickListener {
            binding.quantityLayout.visibility = View.VISIBLE
            binding.addBtn.visibility = View.GONE
            productDetails.quantity = 1
            binding.quantityTxt.text = productDetails.quantity.toString()
            recipesListViewModel.addCartItem(productDetails,
                AddItemReq(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID),productDetails.id)
            )}
        binding.quantityAddBtn.setOnClickListener {
            productDetails.quantity++
            binding.quantityTxt.text = productDetails.quantity.toString()
            recipesListViewModel.addCartItem(productDetails,AddItemReq(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID),productDetails.id))
        }
        binding.quantityMinusBtn.setOnClickListener {
            productDetails.quantity--
            binding.quantityTxt.text = productDetails.quantity.toString()
            if(productDetails.quantity==0){
                binding.quantityLayout.visibility = View.GONE
                binding.addBtn.visibility = View.VISIBLE
                binding.quantityTxt.text = productDetails.quantity.toString()
            }
            recipesListViewModel.removeCartItem(
                productDetails,
                AddItemReq(
                    SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID),
                    productDetails.id
                )
            )}
    }

    private fun observeViewModel() {
        observeSnackBarMessages(recipesListViewModel.showSnackBar)
        observeToast(recipesListViewModel.showToast)

    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}