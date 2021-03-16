package com.jagadish.freshmart.base.listeners

import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem


/**
 * Created by Jagadish
 */

interface RecyclerAddressItemListener {
    fun onItemSelected(recipe : AddAddressReq,position : Int)

    fun onItemRemoveAddress(addressReq: AddAddressReq,position: Int)
}
