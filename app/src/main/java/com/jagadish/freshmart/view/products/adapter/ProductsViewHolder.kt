package com.jagadish.freshmart.view.products.adapter

import android.graphics.Paint
import android.opengl.Visibility
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.jagadish.freshmart.utils.toGone
import com.jagadish.freshmart.utils.toVisible
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class ProductsViewHolder(private val itemBinding: ViewProductItemBinding) : RecyclerView.ViewHolder(
    itemBinding.root
) {

    fun bind(recipesItem: ProductsItem, recyclerItemListener: ProductsRecyclerItemListener) {
        itemBinding.productName.text = recipesItem.name
//        if(recipesItem.quantity != 0)
//            itemBinding.productPrice.text = "₹ ${(recipesItem.price*recipesItem.quantity) - (recipesItem.discount_price* recipesItem.quantity)}"
//        else
            itemBinding.productPrice.text = "₹ ${recipesItem.price - (recipesItem.discount_price)}"
        Glide.with(itemBinding.productImage.context).load(recipesItem.image.tumb.url).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).diskCacheStrategy(
            DiskCacheStrategy.DATA).transform(CenterInside(), RoundedCorners(24)).into(itemBinding.productImage)
        itemBinding.unit.text = recipesItem.unit
        itemBinding.description.text = recipesItem.description
        if(recipesItem.discount_price != 0.00) {
            itemBinding.discountPrice.text = (recipesItem.price).toString()
            itemBinding.discountPrice.paintFlags =
                (itemBinding.discountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
        }
        if(recipesItem.isAddCart){
            itemBinding.quantityLayout.visibility = View.VISIBLE
            itemBinding.addBtn.visibility = View.GONE
            itemBinding.quantityTxt.text = recipesItem.quantity.toString()
        }else{
            itemBinding.quantityLayout.visibility = View.GONE
            itemBinding.addBtn.visibility = View.VISIBLE
            itemBinding.quantityTxt.text = recipesItem.quantity.toString()
        }
        itemBinding.addBtn.setOnClickListener {
            itemBinding.quantityLayout.visibility = View.VISIBLE
            itemBinding.addBtn.visibility = View.GONE
            recyclerItemListener.onItemAddCart(recipesItem)}
        itemBinding.quantityAddBtn.setOnClickListener {
            if(!recipesItem.isLoad) {
                recyclerItemListener.onItemQuantityIncrease(
                    recipesItem
                )
            }
        }
        itemBinding.quantityMinusBtn.setOnClickListener {
            if(!recipesItem.isLoad) {
                recyclerItemListener.onItemQuantityDecrease(
                    recipesItem
                )}}
        itemBinding.root.setOnClickListener { recyclerItemListener.onItemSelected(recipesItem) }
        if(recipesItem.isLoad){
            itemBinding.quantityTxt.toGone()
            itemBinding.quantityProgress.toVisible()
        }else{
            itemBinding.quantityTxt.toVisible()
            itemBinding.quantityProgress.toGone()
        }
        if(!recipesItem.status){
            itemBinding.quantityLayout.visibility = View.GONE
            itemBinding.addBtn.visibility = View.GONE
            itemBinding.outOfStockBackground.visibility = View.VISIBLE
            itemBinding.outOfStockLabel.visibility = View.VISIBLE
            itemBinding.outOfStockBackground.alpha = 0.7f
            itemBinding.outOfStockBackground.setBackgroundColor(itemBinding.productImage.context.resources.getColor(R.color.white))
        }
    }
}

