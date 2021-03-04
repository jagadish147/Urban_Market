package com.jagadish.freshmart.view.main.ui.store.adapter

import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class StoreViewHolder(private val itemBinding: ShopItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: ShopItem, recyclerItemListener: RecyclerItemListener) {
        itemBinding.tvName.text = recipesItem.name
        Picasso.get().load(recipesItem.image).into(itemBinding.categoryImage)
        itemBinding.root.setOnClickListener { recyclerItemListener.onItemSelected(recipesItem) }
    }
}

