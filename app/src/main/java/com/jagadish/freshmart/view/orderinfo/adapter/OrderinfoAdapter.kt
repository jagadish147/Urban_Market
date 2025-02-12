package com.jagadish.freshmart.view.orderinfo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.jagadish.freshmart.view.main.ui.cart.CartViewModel
import com.jagadish.freshmart.view.main.ui.store.StoreViewModel
import com.jagadish.freshmart.view.products.ProductsFragmentViewModel

/**
 * Created by AhmedEltaher
 */

class OrderinfoAdapter(private val recipesListViewModel: CartViewModel, private val recipes: List<ProductsItem>) : RecyclerView.Adapter<OrderinfoViewHolder>() {

    private val onItemClickListener: ProductsRecyclerItemListener = object : ProductsRecyclerItemListener {

        override fun onItemSelected(recipe: ProductsItem) {
            recipesListViewModel.openRecipeDetails(recipe)
        }

        override fun onItemAddCart(productsItem: ProductsItem) {
            recipes[recipes.indexOf(productsItem)].isAddCart = true
          recipes[recipes.indexOf(productsItem)].quantity = 1
            notifyItemChanged(recipes.indexOf(productsItem))
//            recipesListViewModel.checkCartItems(productsItem)
        }

        override fun onItemRemoveCart(productsItem: ProductsItem) {
            recipes[recipes.indexOf(productsItem)].isAddCart = false
            recipes[recipes.indexOf(productsItem)].quantity = 0
            notifyItemChanged(recipes.indexOf(productsItem))
        }

        override fun onItemQuantityIncrease(productsItem: ProductsItem) {
            recipes[recipes.indexOf(productsItem)].quantity++
            notifyItemChanged(recipes.indexOf(productsItem))
        }

        override fun onItemQuantityDecrease(productsItem: ProductsItem) {
            recipes[recipes.indexOf(productsItem)].quantity--
            if(recipes[recipes.indexOf(productsItem)].quantity == 0){
                onItemRemoveCart(productsItem)
            }
            notifyItemChanged(recipes.indexOf(productsItem))
        }
    }
    //https://jsonblob.com/add55310-7c87-11eb-981f-b9bef68ee992
    //https://jsonblob.com/api/add55310-7c87-11eb-981f-b9bef68ee992
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderinfoViewHolder {
        val itemBinding = ViewProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderinfoViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: OrderinfoViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun getSelectedItems() : List<ProductsItem>{
        return recipes
    }
}

