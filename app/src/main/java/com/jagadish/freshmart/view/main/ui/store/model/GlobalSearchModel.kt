package com.jagadish.freshmart.view.main.ui.store.model

import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem

data class GlobalSearchModel (val products: ArrayList<ProductsItem>, val categories : ArrayList<ShopItem>)