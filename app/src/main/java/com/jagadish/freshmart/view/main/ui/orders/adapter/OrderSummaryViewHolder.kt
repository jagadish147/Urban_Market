package com.jagadish.freshmart.view.main.ui.orders.adapter

import android.graphics.Paint
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
import com.jagadish.freshmart.databinding.ViewSummaryProductItemBinding
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class OrderSummaryViewHolder(private val itemBinding: ViewSummaryProductItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: ProductsItem, recyclerItemListener: ProductsRecyclerItemListener) {
        itemBinding.productName.text = recipesItem.name
        itemBinding.productPrice.text = "₹ ${recipesItem.price}"
        itemBinding.unit.text = recipesItem.unit
        if(recipesItem.discount_price >0) {
            itemBinding.discountPrice.text = recipesItem.discount_price.toString()
            itemBinding.discountPrice.paintFlags =
                (itemBinding.discountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
        }else{
            itemBinding.discountPrice.visibility= View.GONE
        }
        itemBinding.description.text = recipesItem.description
        itemBinding.quanitity.text = "Quantity : "+recipesItem.quantity.toString()
        Glide.with(itemBinding.productImage.context).load(recipesItem.image_url).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).diskCacheStrategy(
            DiskCacheStrategy.DATA).transform(CenterInside(), RoundedCorners(24))
            .into(itemBinding.productImage)

    }
}

