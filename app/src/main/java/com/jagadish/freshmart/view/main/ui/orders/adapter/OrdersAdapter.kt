package com.jagadish.freshmart.view.main.ui.orders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.base.listeners.DeliveryBoyOrderListener
import com.jagadish.freshmart.base.listeners.OrderInfoListener
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.AddItemReq
import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
import com.jagadish.freshmart.data.dto.order.OrderItems
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.databinding.ViewOrderItemBinding
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.jagadish.freshmart.databinding.ViewScheduleOrderItemBinding
import com.jagadish.freshmart.view.deliveryboy.ui.dashboard.DashboardViewModel
import com.jagadish.freshmart.view.main.ui.cart.CartViewModel
import com.jagadish.freshmart.view.main.ui.orders.OrdersViewModel
import com.jagadish.freshmart.view.main.ui.store.StoreViewModel
import com.jagadish.freshmart.view.products.ProductsFragmentViewModel

/**
 * Created by AhmedEltaher
 */

class OrdersAdapter(private val recipesListViewModel: OrdersViewModel, private val recipes: List<OrderItems>) : RecyclerView.Adapter<OrderItemsViewHolder>() {

    private val onItemClickListener: OrderInfoListener = object : OrderInfoListener {


        override fun onItemClick(orderItems: OrderItems) {
            recipesListViewModel.onClickOrderItem(orderItems)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemsViewHolder {
        val itemBinding = ViewOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder:  OrderItemsViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun getSelectedItems() : List<OrderItems>{
        return recipes
    }
}

