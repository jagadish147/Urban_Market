package com.jagadish.freshmart.view.deliveryboy

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.databinding.ActivityHomeBinding
import com.jagadish.freshmart.view.deliveryboy.login.DeliveryBoyLoginActivity
import com.jagadish.freshmart.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryHomeActivity : BaseActivity(){

    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)

        val navController = findNavController(R.id.nav_host_fragment)

        binding.navView.setupWithNavController(navController)
        binding.navView.setOnNavigationItemSelectedListener { item ->
            var isNavigate= false
            when (item.itemId) {
                R.id.navigation_dash_board -> {
                    isNavigate = true
                    navController.navigate(R.id.navigation_dash_board)
                }

                R.id.navigation_delivery_profile -> {
                    if(!SharedPreferencesUtils.getBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN)) {
                        isNavigate = false
                        startActivity(Intent(this@DeliveryHomeActivity, DeliveryBoyLoginActivity::class.java))
                    }else {
                        isNavigate = true
                        navController.navigate(R.id.navigation_profile)
                    }
                }
            }
            isNavigate
        }
    }
}