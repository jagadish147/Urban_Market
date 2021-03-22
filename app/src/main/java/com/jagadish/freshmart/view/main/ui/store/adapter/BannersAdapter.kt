package com.jagadish.freshmart.view.main.ui.store.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asksira.loopingviewpager.LoopingPagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jagadish.freshmart.R
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ItemStoreBannerBinding
import com.jagadish.freshmart.utils.shimmer.Shimmer
import com.jagadish.freshmart.utils.shimmer.ShimmerDrawable
import com.jagadish.freshmart.view.main.ui.store.StoreClickListener

/**
 * Created by Jagadeesh on 27-10-2020.
 */
class BannersAdapter(
    context: Context,
    itemList: List<ShopItem>,
    isInfinite: Boolean
) : LoopingPagerAdapter<ShopItem>(context, itemList, isInfinite) {
    lateinit var binding: ItemStoreBannerBinding
    override fun getItemViewType(listPosition: Int): Int {
        return VIEW_TYPE_NORMAL
    }

    override fun inflateView(
        viewType: Int,
        container: ViewGroup,
        listPosition: Int
    ): View {
        binding = ItemStoreBannerBinding.inflate(
            LayoutInflater.from(container.context),
            container,
            false
        )
        return binding.root
    }

    override fun bindView(
        convertView: View,
        listPosition: Int,
        viewType: Int
    ) {
        val shimmer = Shimmer.AlphaHighlightBuilder()
            .setDuration(1800)
            .setBaseAlpha(0.9f)
            .setHighlightAlpha(0.8f)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()
        val shimmerDrawable = ShimmerDrawable().apply { setShimmer(shimmer) }
        Glide.with(context).load(itemList?.get(listPosition)?.image?.url).diskCacheStrategy(
            DiskCacheStrategy.DATA).placeholder(shimmerDrawable).into(
            convertView.findViewById(
                R.id.image
            )
        )
    }


    companion object {
        private const val VIEW_TYPE_NORMAL = 100
    }
}