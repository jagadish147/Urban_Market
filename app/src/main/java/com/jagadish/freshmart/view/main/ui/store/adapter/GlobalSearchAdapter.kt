package com.jagadish.freshmart.view.main.ui.store.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ItemGlobalSearchCategoriesBinding
import com.jagadish.freshmart.databinding.ItemGlobalSearchProductsBinding
import com.jagadish.freshmart.view.main.ui.store.StoreViewModel
import com.jagadish.freshmart.view.main.ui.store.model.GlobalSearchModel

class GlobalSearchAdapter(
    frag: FragmentActivity,
    list: ArrayList<GlobalSearchModel>,
    recipesListViewModel: StoreViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER_LAYOUT = 0
    private val TYPE_FOOTER_LAYOUT = 1
    private val Type_BANNER_LAYOUT = 2
    private val TYPE_PROPERTIES_LAYOUT = 3
    private val TYPE_LATEST_ADDITIONS = 4

    private var mContext: Context = frag.applicationContext
    private var mList = ArrayList<GlobalSearchModel>()
    private var mStoreListViewModel: StoreViewModel = recipesListViewModel


    init {
        mList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            Type_BANNER_LAYOUT -> {
                val bannerView =
                    ItemGlobalSearchProductsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return GlobalSearchProducts(bannerView)
            }
            else -> {
                val propertiesView =
                    ItemGlobalSearchCategoriesBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return GlobalSearchCategories(propertiesView)
            }

        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateData(list: ArrayList<GlobalSearchModel>) {
        mList.clear()
        mList.addAll(list)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (position) {
            0 -> {
                (holder as GlobalSearchProducts).bind(mList[position].products, mStoreListViewModel)
            }
            1 -> {
                (holder as GlobalSearchCategories).bind(
                    mList[position].categories,
                    mStoreListViewModel
                )
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


    class GlobalSearchProducts(
        val binding: ItemGlobalSearchProductsBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(items: List<ProductsItem>, mStoreListViewModel: StoreViewModel) {
            if(items.isNotEmpty()) {
                binding.rvSearchProducts.layoutManager =
                    LinearLayoutManager(binding.root.context)
                binding.rvSearchProducts.adapter = SearchProductsAdapter(mStoreListViewModel, items)
            }else{
                binding.collectionsHeader.visibility = View.GONE
            }
        }
    }

    class GlobalSearchCategories(
        val binding: ItemGlobalSearchCategoriesBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(items: ArrayList<ShopItem>, mStoreListViewModel: StoreViewModel) {
            if(items.isNotEmpty()) {
                binding.rvSearchCategories.layoutManager =
                    LinearLayoutManager(binding.root.context)
                binding.rvSearchCategories.adapter =
                    SearchCategoriesAdapter(mStoreListViewModel, items)
            }else{
                binding.collectionsHeader.visibility = View.GONE
            }
        }

    }
}