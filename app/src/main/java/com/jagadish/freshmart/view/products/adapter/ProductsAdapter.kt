package com.jagadish.freshmart.view.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.AddItemReq
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.jagadish.freshmart.view.main.ui.store.StoreViewModel
import com.jagadish.freshmart.view.products.ProductsFragmentViewModel

/**
 * Created by AhmedEltaher
 */

class ProductsAdapter(private val recipesListViewModel: ProductsFragmentViewModel, private val recipes: MutableList<ProductsItem>) : RecyclerView.Adapter<ProductsViewHolder>() {

    private var filterProducts  = recipes
    private val onItemClickListener: ProductsRecyclerItemListener = object : ProductsRecyclerItemListener {

        override fun onItemSelected(recipe: ProductsItem) {
            recipesListViewModel.openRecipeDetails(recipe)
        }

        override fun onItemAddCart(productsItem: ProductsItem) {
            filterProducts[filterProducts.indexOf(productsItem)].isAddCart = true
            filterProducts[filterProducts.indexOf(productsItem)].quantity = 1
            filterProducts[filterProducts.indexOf(productsItem)].isLoad = true
            notifyItemChanged(filterProducts.indexOf(productsItem))
            recipesListViewModel.addCartItem(productsItem,AddItemReq(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID),productsItem.id))
        }

        override fun onItemRemoveCart(productsItem: ProductsItem) {
            filterProducts[filterProducts.indexOf(productsItem)].isAddCart = false
            filterProducts[filterProducts.indexOf(productsItem)].quantity = 0
            filterProducts[filterProducts.indexOf(productsItem)].isLoad = true
//            recipes.remove(productsItem)
            notifyItemChanged(filterProducts.indexOf(productsItem))
            recipesListViewModel.removeCartItem(productsItem,AddItemReq(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID),productsItem.id))
        }

        override fun onItemQuantityIncrease(productsItem: ProductsItem) {
            filterProducts[filterProducts.indexOf(productsItem)].quantity++
            filterProducts[filterProducts.indexOf(productsItem)].isLoad = true
            notifyItemChanged(filterProducts.indexOf(productsItem))
            recipesListViewModel.addCartItem(productsItem,AddItemReq(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID),productsItem.id))
        }

        override fun onItemQuantityDecrease(productsItem: ProductsItem) {
            if (filterProducts[filterProducts.indexOf(productsItem)].quantity > 0) {
                filterProducts[filterProducts.indexOf(productsItem)].quantity--
                filterProducts[filterProducts.indexOf(productsItem)].isLoad = true
                if (filterProducts[filterProducts.indexOf(productsItem)].quantity == 0) {
                    onItemRemoveCart(productsItem)
                }else{
                    recipesListViewModel.removeCartItem(
                        productsItem,
                        AddItemReq(
                            SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID),
                            productsItem.id
                        )
                    )
                }
                notifyItemChanged(filterProducts.indexOf(productsItem))

            }
        }
    }
    //https://jsonblob.com/add55310-7c87-11eb-981f-b9bef68ee992
    //https://jsonblob.com/api/add55310-7c87-11eb-981f-b9bef68ee992
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val itemBinding = ViewProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(filterProducts[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return filterProducts.size
    }

    fun getItems() : List<ProductsItem>{
        return filterProducts
    }

    fun setFitersItems(items : MutableList<ProductsItem>){
        this.filterProducts = items
        notifyDataSetChanged()
    }

    fun showAllItems(){
        this.filterProducts = recipes
        notifyDataSetChanged()
    }
}

