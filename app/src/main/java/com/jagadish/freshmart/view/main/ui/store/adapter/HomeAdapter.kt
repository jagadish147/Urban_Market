package com.jagadish.freshmart.view.main.ui.store.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.R
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ViewStoreBannersBinding
import com.jagadish.freshmart.databinding.ViewStoreCategoriesBinding
import com.jagadish.freshmart.utils.dp
import com.jagadish.freshmart.utils.getColorCompat
import com.jagadish.freshmart.view.main.ui.store.StoreViewModel
import com.jagadish.freshmart.view.main.ui.store.model.HomeModel
import ru.tinkoff.scrollingpagerindicator.ViewPagerAttacher
import kotlin.collections.ArrayList

/**
 * Created by Jagadeesh on 27-10-2020.
 */
class HomeAdapter(
    frag: FragmentActivity,
    list: ArrayList<HomeModel>,
    recipesListViewModel: StoreViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER_LAYOUT = 0
    private val TYPE_FOOTER_LAYOUT = 1
    private val Type_BANNER_LAYOUT = 2
    private val TYPE_PROPERTIES_LAYOUT = 3
    private val TYPE_LATEST_ADDITIONS = 4

    private var mContext: Context = frag.applicationContext
    private var mList = ArrayList<HomeModel>()
    private var mStoreListViewModel : StoreViewModel = recipesListViewModel


    init {
        mList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            Type_BANNER_LAYOUT -> {
                val bannerView =
                    ViewStoreBannersBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return BannerViewHolder(bannerView)
            }
            else -> {
                val propertiesView =
                    ViewStoreCategoriesBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return OurCollectionsViewHolder(propertiesView)
            }

        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

     fun updateData(list: ArrayList<HomeModel>){
        mList.clear()
        mList.addAll(list)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (position) {
            0 -> {
                (holder as BannerViewHolder).bind(mList[position].banners, mContext)
            }
            1 -> {
                (holder as OurCollectionsViewHolder).bind(mList[position].categories,mStoreListViewModel)
            }

        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> Type_BANNER_LAYOUT
            1 -> TYPE_LATEST_ADDITIONS
            else -> TYPE_FOOTER_LAYOUT
        }
    }



    class BannerViewHolder(
        val binding: ViewStoreBannersBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(items: List<ShopItem>, mContext: Context) {
            binding.viewpager.adapter = BannersAdapter(
                binding.root.context,
                 items,
                false
            )

            //Custom bind indicator
            binding.indicator.highlighterViewDelegate = {
                val highlighter = View(mContext)
                highlighter.layoutParams = FrameLayout.LayoutParams(16.dp(), 2.dp())
                highlighter.setBackgroundColor(mContext.getColorCompat(R.color.white))
                highlighter
            }
            binding.indicator.unselectedViewDelegate = {
                val unselected = View(mContext)
                unselected.layoutParams = LinearLayout.LayoutParams(16.dp(), 2.dp())
                unselected.setBackgroundColor(mContext.getColorCompat(R.color.white))
                unselected.alpha = 0.4f
                unselected
            }
            binding.viewpager.onIndicatorProgress = { selectingPosition, progress -> binding.indicator.onPageScrolled(selectingPosition, progress) }
            binding.indicator.updateIndicatorCounts(binding.viewpager.indicatorCount)
//            if(items.size>1) {
//                binding.bannerIndicator.attachToPager(
//                        binding.viewpager,
//                        ViewPagerAttacher()
//                )
//            }else{
//                binding.bannerIndicator.visibility = View.GONE
//            }
//
//            if(items.isEmpty()){
//                binding.bannerIndicator.visibility = View.GONE
//            }
        }
    }

    class OurCollectionsViewHolder(
        val binding: ViewStoreCategoriesBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(items: ArrayList<ShopItem>, mStoreListViewModel: StoreViewModel) {
            binding.rvHorizontal.layoutManager =
                GridLayoutManager(binding.root.context, 2)
            binding.rvHorizontal.adapter = StoreAdapter(mStoreListViewModel, items)
        }

    }


}