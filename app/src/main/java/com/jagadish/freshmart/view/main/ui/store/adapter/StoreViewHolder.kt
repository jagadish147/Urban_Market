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
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.utils.shimmer.Shimmer
import com.jagadish.freshmart.utils.shimmer.ShimmerDrawable
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class StoreViewHolder(private val itemBinding: ShopItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: ShopItem, recyclerItemListener: RecyclerItemListener) {
        itemBinding.tvName.text = recipesItem.name
        val shimmer = Shimmer.AlphaHighlightBuilder()
            .setDuration(1800)
            .setBaseAlpha(0.9f)
            .setHighlightAlpha(0.8f)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()
        val shimmerDrawable = ShimmerDrawable().apply { setShimmer(shimmer) }
        Glide.with(itemBinding.categoryImage.context).load(recipesItem.image.url)
            .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).transform(CenterCrop(),RoundedCorners(24))
            .diskCacheStrategy(
                DiskCacheStrategy.DATA).placeholder(shimmerDrawable).into(itemBinding.categoryImage)
        itemBinding.tvBack.alpha = 0.4f
        itemBinding.tvBack.setBackground(itemBinding.categoryImage.context.resources.getDrawable(R.drawable.txt_bg))
        itemBinding.root.setOnClickListener { recyclerItemListener.onItemSelected(recipesItem) }
    }
}

