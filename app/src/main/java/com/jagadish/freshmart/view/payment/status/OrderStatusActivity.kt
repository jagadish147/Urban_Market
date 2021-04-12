package com.jagadish.freshmart.view.payment.status

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.jagadish.freshmart.ORDER_DETAILS
import com.jagadish.freshmart.PAYMENT_DETAILS
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.databinding.ActivityOrderStatusBinding
import com.jagadish.freshmart.view.main.MainActivity
import com.jagadish.freshmart.view.payment.PaymentDetailsModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
@AndroidEntryPoint
class OrderStatusActivity : BaseActivity() {

    private lateinit var binding:ActivityOrderStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_status)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Order Status"
        val paymentDetails =  intent.getParcelableExtra<PaymentDetailsModel>(ORDER_DETAILS)
        binding.orderdetails = paymentDetails
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        binding.orderdate = currentDate
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@OrderStatusActivity, MainActivity::class.java))
        finish()
    }
}