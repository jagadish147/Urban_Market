package com.jagadish.freshmart.base.listeners

import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.deliver.orders.ScheduleOrders

interface DeliveryBoyOrderListener {
    fun onItemClick(scheduleOrders: ScheduleOrders)
}