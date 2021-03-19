package com.jagadish.freshmart.view.main.ui.store.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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
        Glide.with(itemBinding.categoryImage.context).load(recipesItem.image.tumb.url).transform(CenterInside(),RoundedCorners(24))
            .into(itemBinding.categoryImage)
        itemBinding.root.setOnClickListener { recyclerItemListener.onItemSelected(recipesItem) }
    }
}

