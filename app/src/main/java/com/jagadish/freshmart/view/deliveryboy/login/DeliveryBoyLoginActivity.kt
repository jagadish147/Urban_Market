package com.jagadish.freshmart.view.deliveryboy.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.CreateCartRes
import com.jagadish.freshmart.data.dto.products.ProductsItem
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.ActivityDeliveryLoginBinding
import com.jagadish.freshmart.databinding.ActivityLoginBinding
import com.jagadish.freshmart.databinding.ActivitySplashBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.intro.IntroSliderActivity
import com.jagadish.freshmart.view.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class DeliveryBoyLoginActivity : BaseActivity() {

    private lateinit var binding: ActivityDeliveryLoginBinding
    private val loginViewModel: DeliveryLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_delivery_login)

        login_back_arrow.setOnClickListener {
            onBackPressed()
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}
