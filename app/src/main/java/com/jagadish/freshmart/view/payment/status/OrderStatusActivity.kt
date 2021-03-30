package com.jagadish.freshmart.view.payment.status

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jagadish.freshmart.ORDER_DETAILS
import com.jagadish.freshmart.PAYMENT_DETAILS
import com.jagadish.freshmart.R
import com.jagadish.freshmart.databinding.ActivityOrderStatusBinding
import com.jagadish.freshmart.view.payment.PaymentDetailsModel
import java.text.SimpleDateFormat
import java.util.*

class OrderStatusActivity : AppCompatActivity() {

    private lateinit var binding:ActivityOrderStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_status)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        val paymentDetails =  intent.getParcelableExtra<PaymentDetailsModel>(ORDER_DETAILS)
        binding.orderdetails = paymentDetails
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        binding.orderdate = currentDate
    }
}