package com.jagadish.freshmart.base.listeners

import com.jagadish.freshmart.data.dto.shop.ShopItem


/**
 * Created by Jagadish
 */

interface RecyclerItemListener {
    fun onItemSelected(recipe : ShopItem)
}
