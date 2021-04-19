package com.jagadish.freshmart.view.payment.status

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.jagadish.freshmart.*
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
        val paymentMode = intent.getBooleanExtra(CASH_ON_DELIVERY,false)
        val paymentStatus = intent.getBooleanExtra(PAYMENT_STATUS,false)
        binding.orderdetails = paymentDetails
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        binding.orderdate = currentDate

        if(!paymentMode) {
            binding.paymentStatusTxt.text =
                if (paymentStatus) "Payment Successful" else "Payment Failed"
            binding.paymentStatusImage.setAnimation(if (paymentStatus) R.raw.payment_confirm else R.raw.payment_failed)
        }else{
            binding.paymentStatusTxt.text = "Cash On Delivery"
        }

        binding.gotostoreBtn.setOnClickListener {  onBackPressed()}
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
        var intent = Intent(this@OrderStatusActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}