package com.jagadish.freshmart.view.products.adapter

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class ProductsViewHolder(private val itemBinding: ViewProductItemBinding) : RecyclerView.ViewHolder(
    itemBinding.root
) {

    fun bind(recipesItem: ProductsItem, recyclerItemListener: ProductsRecyclerItemListener) {
        itemBinding.productName.text = recipesItem.name
        itemBinding.productPrice.text = "₹ ${recipesItem.price}"
        Picasso.get().load(recipesItem.image.tumb.url).into(itemBinding.productImage)
        itemBinding.unit.text = recipesItem.unit
        itemBinding.description.text = recipesItem.description
        if(recipesItem.discount_price != 0.00) {
            itemBinding.discountPrice.text = recipesItem.discount_price.toString()
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
        itemBinding.quantityAddBtn.setOnClickListener { recyclerItemListener.onItemQuantityIncrease(
            recipesItem
        ) }
        itemBinding.quantityMinusBtn.setOnClickListener { recyclerItemListener.onItemQuantityDecrease(
            recipesItem
        ) }
//        itemBinding.root.setOnClickListener { recyclerItemListener.onItemSelected(recipesItem) }
    }
}

