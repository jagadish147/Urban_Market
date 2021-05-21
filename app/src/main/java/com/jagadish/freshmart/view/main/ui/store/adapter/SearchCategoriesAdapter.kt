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

class SearchCategoriesAdapter(private val recipesListViewModel: StoreViewModel, private val recipes: List<ShopItem>) : RecyclerView.Adapter<SearchCategoriesViewHolder>() {

    private val onItemClickListener: StoreGlobalSearchListener = object : StoreGlobalSearchListener {


        override fun onClickProduct(recipesItem: ProductsItem) {
            TODO("Not yet implemented")
        }

        override fun onCLickCategory(recipesItem: ShopItem) {
            recipesListViewModel.openRecipeDetails(recipesItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCategoriesViewHolder {
        val itemBinding = ItemSrarchProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchCategoriesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchCategoriesViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}

