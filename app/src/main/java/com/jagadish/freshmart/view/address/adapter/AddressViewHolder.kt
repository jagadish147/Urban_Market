package com.jagadish.freshmart.view.address.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.base.listeners.RecyclerAddressItemListener
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.address.AddAddressRes
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.databinding.ViewAddressItemBinding
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class AddressViewHolder(private val itemBinding: ViewAddressItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: AddAddressReq, recyclerItemListener: RecyclerAddressItemListener) {
        itemBinding.addressLabel1.text = recipesItem.address_line1
        itemBinding.addressLabel2.text = recipesItem.address_line2



        itemBinding.root.setOnClickListener { recyclerItemListener.onItemSelected(recipesItem) }
    }
}

