package com.jagadish.freshmart.view.deliveryboy.ui.dashboard.adapter

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.listeners.DeliveryBoyOrderListener
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.jagadish.freshmart.databinding.ViewScheduleOrderItemBinding
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class ScheduleOrderItemsViewHolder(private val itemBinding: ViewScheduleOrderItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: ScheduleOrders, recyclerItemListener: DeliveryBoyOrderListener) {
        itemBinding.orderNumber.text = "Order Number : "+recipesItem.number
        itemBinding.orderStatus.text = recipesItem.status
        itemBinding.customerName.text = "Name : "+recipesItem.customer.name
        itemBinding.customerMobile.text = "Mobile : "+recipesItem.customer.phone
        itemBinding.paymentMode.text = "Payment Mode : "+recipesItem.order_type

        when(recipesItem.status){
            "Accepted"->{
                itemBinding.orderStatus.setTextColor(ContextCompat.getColor(itemBinding.root.context,R.color.teal_700))
            }
            "Cancelled"->{
                itemBinding.orderStatus.setTextColor(ContextCompat.getColor(itemBinding.root.context,R.color.red))
            }
            "Rejected"->{
                itemBinding.orderStatus.setTextColor(ContextCompat.getColor(itemBinding.root.context,R.color.red))
            }
        }

        itemView.setOnClickListener {
            recyclerItemListener.onItemClick(recipesItem)
        }
    }
}

