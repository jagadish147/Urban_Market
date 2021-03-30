package com.jagadish.freshmart.view.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import com.atom.mpsdklibrary.PayActivity
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.*
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.order.PaymentStatusReq
import com.jagadish.freshmart.data.dto.order.PaymentStatusRes
import com.jagadish.freshmart.databinding.ActivityOrderPlaceBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.orderinfo.OrderInfoActivity
import com.jagadish.freshmart.view.payment.status.OrderStatusActivity
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class OrderPlaceActivity : BaseActivity() {

    private lateinit var binding:ActivityOrderPlaceBinding
    private val orderPlaceViewModel: OrderPlaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_place)
        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

      val paymentDetails =  intent.getParcelableExtra<PaymentDetailsModel>(PAYMENT_DETAILS)
        binding.orderDetails = paymentDetails

        observeViewModel()

        binding.placeOrder.setOnClickListener {

            when {
                binding.cashOnDelivery.isChecked -> {
                    val paymentstatusReq = PaymentStatusReq(paymentDetails!!.orderres.order_id, "Cash", "123456")
                    orderPlaceViewModel.checkPaymentStatus(paymentstatusReq)
                }
                binding.onlinePayment.isChecked -> {
                    payNow()

                }
                else -> {
                    Validator.setError(binding.placeOrder,"Please Select Payment Mode")
                }
            }
        }
    }

    private fun observeViewModel() {
        observeSnackBarMessages(orderPlaceViewModel.showSnackBar)
        observeToast(orderPlaceViewModel.showToast)
        observe(orderPlaceViewModel.paymentStatusLiveData, ::handlePaymentStatus)
    }
    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
    }

    private fun showDataView(show: Boolean) {
        binding.pbLoading.toGone()
    }

    private fun handlePaymentStatus(status: Resource<PaymentStatusRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindPaymentStatus(paymentStatusRes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { orderPlaceViewModel.showToastMessage(it) }
            }
        }
    }

    private fun bindPaymentStatus(paymentStatusRes: PaymentStatusRes){
        if(paymentStatusRes.success && paymentStatusRes.status == 200){
            val nextScreenIntent = Intent(this, OrderStatusActivity::class.java).apply {
                putExtra(ORDER_DETAILS, binding.orderDetails)
                putExtra(PAYMENT_STATUS, true)
            }
            startActivity(nextScreenIntent)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if (requestCode == 1) {
            println("---------INSIDE-------")
            if (data != null) {
                val message = data.getStringExtra("status")
                val resKey = data.getStringArrayExtra("responseKeyArray")
                val resValue = data.getStringArrayExtra("responseValueArray")
                if (resKey != null && resValue != null) {
                    val paymentstatusReq = PaymentStatusReq(binding.orderDetails!!.orderres.order_id, "Card", "123456")
                    orderPlaceViewModel.checkPaymentStatus(paymentstatusReq)
                    for (i in resKey.indices)
                        println("  " + i + " resKey : " + resKey[i] + " resValue : " + resValue[i])
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                println("RECEIVED BACK--->$message")
            }
        }
    }

    //Payment gateway
    fun payNow() {
        Toast.makeText(this, "Pay now clicked", Toast.LENGTH_LONG).show();
        val newPayIntent= Intent(this, PayActivity:: class.java);
        newPayIntent.putExtra("isLive", false); //true for Live
        newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
        newPayIntent.putExtra("merchantId", "197");
        newPayIntent.putExtra("loginid","197" );
        newPayIntent.putExtra("password", "Test@123");//NCA@1234
        newPayIntent.putExtra("prodid", "NSE");//NCA
        newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
        newPayIntent.putExtra("clientcode",encodeBase64( "007") );
        newPayIntent.putExtra("custacc","100000036600" );
        newPayIntent.putExtra("channelid", "INT");
        newPayIntent.putExtra("amt", binding.orderDetails!!.orderTotalPayable.toString() ); //Should be 2 decimal number i.e 1.00
        newPayIntent.putExtra("txnid", binding.orderDetails!!.orderres!!.order_id); //013
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        newPayIntent.putExtra("date", currentDate);//Should be in same format
        newPayIntent.putExtra("signature_request","KEY123657234" );
        newPayIntent.putExtra("signature_response","KEYRESP123657234" );
        newPayIntent.putExtra("discriminator","All");//NB,All
        newPayIntent.putExtra("ru","https://paynetzuat.atomtech.in/mobilesdk/param");
        //Optinal Parameters
        newPayIntent.putExtra("customerName", binding.orderDetails!!.userName);//Only for Name
        newPayIntent.putExtra("customerEmailID", SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_EMAIL));//Only for Email ID
        newPayIntent.putExtra("customerMobileNo",binding.orderDetails!!.userMobile );//Only for Mobile No.
        newPayIntent.putExtra("billingAddress", binding.orderDetails!!.orderDeliveryAddress!!.address_line1);//Only for Address
        newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 2");// Can pass any data
        // Pass data in XML format, only for Multi product
        startActivityForResult(newPayIntent,1);

    }


    fun encodeBase64(encode: String): String? {
        println("[encodeBase64] Base64 encode : $encode")
        var decode: String? = null
        try {
            decode = Base64.encode(encode.toByteArray())
        } catch (e: Exception) {
            println("Unable to decode : $e")
        }
        return decode
    }
}