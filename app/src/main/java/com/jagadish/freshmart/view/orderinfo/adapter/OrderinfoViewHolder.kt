package com.jagadish.freshmart.view.orderinfo.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class OrderinfoViewHolder(private val itemBinding: ViewProductItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: ProductsItem, recyclerItemListener: ProductsRecyclerItemListener) {
        itemBinding.productName.text = recipesItem.name
        itemBinding.productPrice.text = "₹ ${recipesItem.price}"
        itemBinding.unit.text = recipesItem.unit
        itemBinding.description.text = recipesItem.description
        Glide.with(itemBinding.productImage.context).load(recipesItem.image_url).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).diskCacheStrategy(
            DiskCacheStrategy.DATA).transform(CenterInside(), RoundedCorners(24))
            .into(itemBinding.productImage)
            itemBinding.quantityLayout.visibility = View.GONE
            itemBinding.addBtn.visibility = View.GONE
            itemBinding.quantityTxt.text = recipesItem.quantity.toString()

        itemBinding.addBtn.setOnClickListener {
            itemBinding.quantityLayout.visibility = View.GONE
            itemBinding.addBtn.visibility = View.GONE
            recyclerItemListener.onItemAddCart(recipesItem)}
        itemBinding.quantityAddBtn.setOnClickListener { recyclerItemListener.onItemQuantityIncrease(recipesItem) }
        itemBinding.quantityMinusBtn.setOnClickListener { recyclerItemListener.onItemQuantityDecrease(recipesItem) }
//        itemBinding.root.setOnClickListener { recyclerItemListener.onItemSelected(recipesItem) }
    }
}

