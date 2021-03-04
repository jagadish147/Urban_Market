package com.jagadish.freshmart.base.listeners

import com.jagadish.freshmart.data.dto.products.ProductsItem

interface ProductsRecyclerItemListener {
    fun onItemSelected(productsItem: ProductsItem)

}