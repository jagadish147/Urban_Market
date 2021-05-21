package com.jagadish.freshmart.view.main.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.base.listeners.StoreGlobalSearchListener
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ItemSrarchProductBinding
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.view.main.ui.store.StoreViewModel

/**
 * Created by AhmedEltaher
 */

class SearchProductsAdapter(private val recipesListViewModel: StoreViewModel, private val recipes: List<ProductsItem>) : RecyclerView.Adapter<SearchProductsViewHolder>() {

    private val onItemClickListener: StoreGlobalSearchListener = object : StoreGlobalSearchListener {


        override fun onClickProduct(recipesItem: ProductsItem) {
            recipesListViewModel.openProductDetailsPage(recipesItem)
        }

        override fun onCLickCategory(recipesItem: ShopItem) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductsViewHolder {
        val itemBinding = ItemSrarchProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchProductsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchProductsViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}

