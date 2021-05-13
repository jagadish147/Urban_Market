package com.jagadish.freshmart.view.payment

import android.content.Intent
import android.os.Bundle
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
import com.jagadish.freshmart.data.dto.cart.Cart
import com.jagadish.freshmart.data.dto.order.OrderRes
import com.jagadish.freshmart.data.dto.order.PaymentStatusReq
import com.jagadish.freshmart.data.dto.order.PaymentStatusRes
import com.jagadish.freshmart.databinding.ActivityOrderPlaceBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.payment.status.OrderStatusActivity
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class OrderPlaceActivity : BaseActivity() {

    private lateinit var binding:ActivityOrderPlaceBinding
    private val orderPlaceViewModel: OrderPlaceViewModel by viewModels()
    var paymentStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_place)
        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

      val paymentDetails =  intent.getParcelableExtra<PaymentDetailsModel>(PAYMENT_DETAILS)
        binding.orderDetails = paymentDetails

        binding.estimatedTimeValue.text = SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_DELIVERY_IMP_MESSAGE)


        observeViewModel()

        binding.placeOrder.setOnClickListener {
            when {
                binding.cashOnDelivery.isChecked -> {
                    orderPlaceViewModel.createOrderId(binding.orderDetails!!.orderDeliveryAddress.id)
                }
                binding.onlinePayment.isChecked -> {
                    orderPlaceViewModel.createOrderId(binding.orderDetails!!.orderDeliveryAddress.id)
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
        observe(orderPlaceViewModel.orderIdLiveData, ::handleOrderId)
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

    private fun handleOrderId(status: Resource<OrderRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let {
                binding.orderDetails!!.orderres = it
                when {
                    binding.cashOnDelivery.isChecked -> {
                        val paymentstatusReq = PaymentStatusReq(it.order_id, "Cash", it.order_id)
                        orderPlaceViewModel.checkPaymentStatus(paymentstatusReq)
                    }
                    binding.onlinePayment.isChecked -> {
                        payNow(it)
                    }
                    else -> {
                        Validator.setError(binding.placeOrder,"Please Select Payment Mode")
                    }
                }
            }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { orderPlaceViewModel.showToastMessage(it) }
            }
        }
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
            Singleton.getInstance().cart = Cart()
            val nextScreenIntent = Intent(this, OrderStatusActivity::class.java).apply {
                putExtra(ORDER_DETAILS, binding.orderDetails)
                putExtra(PAYMENT_STATUS, paymentStatus)
                putExtra(CASH_ON_DELIVERY,binding.cashOnDelivery.isChecked)
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
                    for (i in resKey.indices){
                        if(resKey[i].equals("f_code")){
                            if(resValue[i].equals("success_00")){
                                onSuccesPayment(resKey,resValue)
                                break
                            }else if(resValue[i].equals("F_05")){
                                onFailedPayment(resKey,resValue)
                                break
                            }else if(resValue[i].equals("C_06")){
                                finish()
                                break
                            }
                        }
                    }
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onSuccesPayment(resKey: Array<String>, resValue: Array<String>) {
        for (i in resKey.indices) {
            if (resKey[i].equals("bank_txn")) {
                paymentStatus = true
                val paymentstatusReq = PaymentStatusReq(binding.orderDetails!!.orderres.order_id, "Card", resValue[i],"Success")
                    orderPlaceViewModel.checkPaymentStatus(paymentstatusReq)
            }
        }
    }

    private fun onFailedPayment(resKey: Array<String>, resValue: Array<String>) {
        for (i in resKey.indices) {
            if (resKey[i].equals("bank_txn")) {
                paymentStatus = false
                val paymentstatusReq = PaymentStatusReq(binding.orderDetails!!.orderres.order_id, "Card", resValue[i],"Failed")
                    orderPlaceViewModel.checkPaymentStatus(paymentstatusReq)
            }
        }
    }

    //Payment gateway
    fun payNow(orderRes: OrderRes) {
        val newPayIntent= Intent(this, PayActivity:: class.java);
        newPayIntent.putExtra("isLive", true); //true for Live
        newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
        newPayIntent.putExtra("merchantId", "122649");
        newPayIntent.putExtra("loginid","122649" );
        newPayIntent.putExtra("password", "4818313e");//NCA@1234
        newPayIntent.putExtra("prodid", "MARKET");//NCA
        newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
        newPayIntent.putExtra("clientcode",encodeBase64( "007") );
        newPayIntent.putExtra("custacc","100000036600" );
        newPayIntent.putExtra("channelid", "INT");
        newPayIntent.putExtra("amt", binding.orderDetails!!.orderTotalPayable.toString() ); //Should be 2 decimal number i.e 1.00
        newPayIntent.putExtra("txnid", binding.orderDetails!!.orderres!!.order_id); //013
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        newPayIntent.putExtra("date", currentDate);//Should be in same format
        newPayIntent.putExtra("signature_request","cc261852543518a0ac" );
        newPayIntent.putExtra("signature_response","08e6b9de7f03db0054" );
        newPayIntent.putExtra("discriminator","All");//NB,All
        newPayIntent.putExtra("ru","https://payment.atomtech.in/paynetz/epi/fts");
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