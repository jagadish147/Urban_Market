package com.jagadish.freshmart.base.listeners

import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem

interface StoreGlobalSearchListener {

    fun onClickProduct(recipesItem: ProductsItem)

    fun onCLickCategory(recipesItem: ShopItem)
}