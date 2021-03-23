package com.jagadish.freshmart.view.deliveryboy

import android.os.Bundle
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryHomeActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}