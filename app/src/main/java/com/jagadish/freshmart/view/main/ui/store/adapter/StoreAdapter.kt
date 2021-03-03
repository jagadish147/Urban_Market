package com.jagadish.freshmart.view.main.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.view.main.ui.store.StoreViewModel

/**
 * Created by AhmedEltaher
 */

class StoreAdapter(private val recipesListViewModel: StoreViewModel, private val recipes: List<ShopItem>) : RecyclerView.Adapter<StoreViewHolder>() {

    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(recipe: ShopItem) {
            recipesListViewModel.openRecipeDetails(recipe)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val itemBinding = ShopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}

