package com.jagadish.freshmart.base.listeners

import com.jagadish.freshmart.data.dto.products.ProductsItem

interface ProductsRecyclerItemListener {
    fun onItemSelected(productsItem: ProductsItem)
    fun onItemAddCart(productsItem: ProductsItem)
    fun onItemRemoveCart(productsItem: ProductsItem)
    fun onItemQuantityIncrease(productsItem: ProductsItem)
    fun onItemQuantityDecrease(productsItem: ProductsItem)
}