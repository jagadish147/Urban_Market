package com.jagadish.freshmart.view.main.ui.orders.adapter

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.listeners.DeliveryBoyOrderListener
import com.jagadish.freshmart.base.listeners.OrderInfoListener
import com.jagadish.freshmart.base.listeners.ProductsRecyclerItemListener
import com.jagadish.freshmart.base.listeners.RecyclerItemListener
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
import com.jagadish.freshmart.data.dto.order.OrderItems
import com.jagadish.freshmart.data.dto.order.OrdersRes
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.dto.shop.ShopItem
import com.jagadish.freshmart.databinding.ShopItemBinding
import com.jagadish.freshmart.databinding.ViewOrderItemBinding
import com.jagadish.freshmart.databinding.ViewProductItemBinding
import com.jagadish.freshmart.databinding.ViewScheduleOrderItemBinding
import com.squareup.picasso.Picasso

/**
 * Created by Jagadish
 */

class OrderItemsViewHolder(private val itemBinding: ViewOrderItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: OrderItems, recyclerItemListener: OrderInfoListener) {
        itemBinding.orderNumber.text = "Order Number : "+recipesItem.number
        itemBinding.orderDate.text = "Order Date : "+recipesItem.order_date
        itemBinding.deliveryDate.text = "Delivery Date : "+recipesItem.delivery_date
        itemBinding.price.text = "Rs : "+(recipesItem.price.toDouble()+recipesItem.delivery_charge)
        itemBinding.itemsCount.text = recipesItem.item_count.toString()+ " items"
        when(recipesItem.status){
            "Accepted"->{
                itemBinding.orderStatus.setTextColor(ContextCompat.getColor(itemBinding.root.context,R.color.teal_700))
            }
            "Finished"->{
                itemBinding.orderStatus.setTextColor(ContextCompat.getColor(itemBinding.root.context,R.color.teal_700))
            }
            "Cancelled"->{
                itemBinding.orderStatus.setTextColor(ContextCompat.getColor(itemBinding.root.context,R.color.red))
            }
            "Rejected"->{
                itemBinding.orderStatus.setTextColor(ContextCompat.getColor(itemBinding.root.context,R.color.red))
            }
            "New"->{
                itemBinding.orderStatus.setTextColor(ContextCompat.getColor(itemBinding.root.context,R.color.yellow))
            }
            "Scheduled"->{
                itemBinding.orderStatus.setTextColor(ContextCompat.getColor(itemBinding.root.context,R.color.yellow))
            }
        }
        if(recipesItem.status.isNotEmpty()){
            itemBinding.orderStatus.text = recipesItem.status
        }

        itemView.setOnClickListener {
            recyclerItemListener.onItemClick(recipesItem)
        }
    }
}

