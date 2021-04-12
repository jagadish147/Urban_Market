package com.jagadish.freshmart.view.main.ui.cart.adapter

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
import com.jagadish.freshmart.view.main.ui.cart.CartViewModel
import com.jagadish.freshmart.view.main.ui.store.StoreViewModel
import com.jagadish.freshmart.view.products.ProductsFragmentViewModel

/**
 * Created by AhmedEltaher
 */

class CartItemsAdapter(private val recipesListViewModel: CartViewModel, private val recipes: ArrayList<ProductsItem>) : RecyclerView.Adapter<CartItemsViewHolder>() {

    private val onItemClickListener: ProductsRecyclerItemListener = object : ProductsRecyclerItemListener {

        override fun onItemSelected(recipe: ProductsItem) {
            recipesListViewModel.openRecipeDetails(recipe)
        }

        override fun onItemAddCart(productsItem: ProductsItem) {
            recipes[recipes.indexOf(productsItem)].isAddCart = true
          recipes[recipes.indexOf(productsItem)].quantity = 1
            notifyItemChanged(recipes.indexOf(productsItem))
            recipesListViewModel.addCartItem(
                AddItemReq(
                    SharedPreferencesUtils.getIntPreference(
                        SharedPreferencesUtils.PREF_DEVICE_CART_ID),productsItem.id)
            )
        }

        override fun onItemRemoveCart(productsItem: ProductsItem) {
            recipes[recipes.indexOf(productsItem)].isAddCart = false
            recipes[recipes.indexOf(productsItem)].quantity = 0

            recipes.remove(productsItem)
            notifyDataSetChanged()
            recipesListViewModel.removeCartItem(AddItemReq(SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID),productsItem.id))
        }

        override fun onItemQuantityIncrease(productsItem: ProductsItem) {
            recipes[recipes.indexOf(productsItem)].quantity++
            notifyItemChanged(recipes.indexOf(productsItem))
            recipesListViewModel.addCartItem(
                AddItemReq(
                    SharedPreferencesUtils.getIntPreference(
                        SharedPreferencesUtils.PREF_DEVICE_CART_ID),productsItem.id)
            )
        }

        override fun onItemQuantityDecrease(productsItem: ProductsItem) {
            if (recipes[recipes.indexOf(productsItem)].quantity > 0) {
                recipes[recipes.indexOf(productsItem)].quantity--
                if (recipes[recipes.indexOf(productsItem)].quantity == 0) {
                    onItemRemoveCart(productsItem)
                }else{
                    recipesListViewModel.removeCartItem(
                        AddItemReq(
                            SharedPreferencesUtils.getIntPreference(
                                SharedPreferencesUtils.PREF_DEVICE_CART_ID
                            ), productsItem.id
                        )
                    )
                }
                notifyItemChanged(recipes.indexOf(productsItem))
            }
        }
    }
    //https://jsonblob.com/add55310-7c87-11eb-981f-b9bef68ee992
    //https://jsonblob.com/api/add55310-7c87-11eb-981f-b9bef68ee992
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemsViewHolder {
        val itemBinding = ViewProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CartItemsViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun getSelectedItems() : List<ProductsItem>{
        return recipes
    }
}

