package com.jagadish.freshmart.view.orderinfo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import com.jagadish.freshmart.BuildConfig
import com.jagadish.freshmart.IS_COME_PROFILE
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_info)
        setSupportActionBar(findViewById(R.id.toolbar))

        if(BuildConfig.FLAVOR == "user") {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_order_info_to_order_list)
        }else{
            if(intent.getBooleanExtra(IS_COME_PROFILE, false)){
                supportActionBar!!.title = "Orders"
            }

        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}