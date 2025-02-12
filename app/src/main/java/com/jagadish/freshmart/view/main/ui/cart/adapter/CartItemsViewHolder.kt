package com.jagadish.freshmart.view.main.ui.cart.adapter

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.jagadish.freshmart.utils.toGone
import com.jagadish.freshmart.utils.toVisible
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class CartItemsViewHolder(private val itemBinding: ViewProductItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: ProductsItem, recyclerItemListener: ProductsRecyclerItemListener) {
        itemBinding.productName.text = recipesItem.name
        if(recipesItem.quantity != 0)
            itemBinding.productPrice.text = "₹ ${recipesItem.price- (recipesItem.discount_price* recipesItem.quantity)}"
        else
            itemBinding.productPrice.text = "₹ ${recipesItem.price- (recipesItem.discount_price)}"
        Glide.with(itemBinding.productImage.context).load(recipesItem.image.tumb.url).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).diskCacheStrategy(
            DiskCacheStrategy.DATA).into(itemBinding.productImage)
        itemBinding.unit.text = recipesItem.unit
        itemBinding.description.text = recipesItem.description
        if(recipesItem.discount_price != 0.00) {
//            if(recipesItem.quantity != 0)
//                itemBinding.discountPrice.text = "₹ ${(recipesItem.price)}"
//            else
            itemBinding.discountPrice.text = "₹"+(recipesItem.price).toString()
            itemBinding.discountPrice.paintFlags =
                (itemBinding.discountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
        }
            itemBinding.quantityLayout.visibility = View.VISIBLE
            itemBinding.addBtn.visibility = View.GONE
            itemBinding.quantityTxt.text = recipesItem.quantity.toString()

        itemBinding.addBtn.setOnClickListener {
            itemBinding.quantityLayout.visibility = View.VISIBLE
            itemBinding.addBtn.visibility = View.GONE
            recyclerItemListener.onItemAddCart(recipesItem)}
        itemBinding.quantityAddBtn.setOnClickListener {
            if(!recipesItem.isLoad) {
                updateLoaderQuantity(true)
                recyclerItemListener.onItemQuantityIncrease(recipesItem)
            }}
        itemBinding.quantityMinusBtn.setOnClickListener {
            if(!recipesItem.isLoad) {
                updateLoaderQuantity(true)
                recyclerItemListener.onItemQuantityDecrease(recipesItem)
            }}
//        itemBinding.root.setOnClickListener { recyclerItemListener.onItemSelected(recipesItem) }
        updateLoaderQuantity(recipesItem.isLoad)
    }

    private fun updateLoaderQuantity(isLoad : Boolean){
        if(isLoad){
            itemBinding.quantityTxt.toGone()
            itemBinding.quantityProgress.toVisible()
        }else{
            itemBinding.quantityTxt.toVisible()
            itemBinding.quantityProgress.toGone()
        }
    }
}

