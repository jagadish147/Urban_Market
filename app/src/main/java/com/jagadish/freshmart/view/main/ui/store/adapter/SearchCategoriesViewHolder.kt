package com.jagadish.freshmart.view.main.ui.store.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.base.listeners.StoreGlobalSearchListener
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ItemSrarchProductBinding
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.utils.shimmer.Shimmer
import com.jagadish.freshmart.utils.shimmer.ShimmerDrawable
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class SearchCategoriesViewHolder(private val itemBinding: ItemSrarchProductBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: ShopItem, recyclerItemListener: StoreGlobalSearchListener) {
        itemBinding.nameTxt.text = recipesItem.name

        itemBinding.root.setOnClickListener {
            recyclerItemListener.onCLickCategory(recipesItem)
        }
    }
}

