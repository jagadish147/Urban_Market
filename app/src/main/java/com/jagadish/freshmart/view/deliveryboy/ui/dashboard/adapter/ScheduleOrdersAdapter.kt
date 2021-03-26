package com.jagadish.freshmart.view.deliveryboy.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.base.listeners.DeliveryBoyOrderListener
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.AddItemReq
import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.jagadish.freshmart.databinding.ViewScheduleOrderItemBinding
import com.jagadish.freshmart.view.deliveryboy.ui.dashboard.DashboardViewModel
import com.jagadish.freshmart.view.main.ui.cart.CartViewModel
import com.jagadish.freshmart.view.main.ui.store.StoreViewModel
import com.jagadish.freshmart.view.products.ProductsFragmentViewModel

/**
 * Created by AhmedEltaher
 */

class ScheduleOrdersAdapter(private val recipesListViewModel: DashboardViewModel, private val recipes: List<ScheduleOrders>) : RecyclerView.Adapter<ScheduleOrderItemsViewHolder>() {

    private val onItemClickListener: DeliveryBoyOrderListener = object : DeliveryBoyOrderListener {


        override fun onItemClick(scheduleOrders: ScheduleOrders) {
            recipesListViewModel.onClickOrderInfo(scheduleOrders)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleOrderItemsViewHolder {
        val itemBinding = ViewScheduleOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleOrderItemsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ScheduleOrderItemsViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun getSelectedItems() : List<ScheduleOrders>{
        return recipes
    }
}

