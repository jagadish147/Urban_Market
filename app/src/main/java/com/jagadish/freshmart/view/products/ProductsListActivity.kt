package com.jagadish.freshmart.view.products

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.jagadish.freshmart.CATEGORY_KEY
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.data.dto.shop.ShopItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)
        setSupportActionBar(findViewById(R.id.toolbar))


        val shopItem = intent.getParcelableExtra<ShopItem>(CATEGORY_KEY)
        supportActionBar!!.title = shopItem!!.name
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}