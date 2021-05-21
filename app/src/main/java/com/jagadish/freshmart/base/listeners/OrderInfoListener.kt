package com.jagadish.freshmart.base.listeners

import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders
import com.jagadish.freshmart.data.dto.order.OrderItems

interface OrderInfoListener {
    fun onItemClick(orderItems: OrderItems)
}