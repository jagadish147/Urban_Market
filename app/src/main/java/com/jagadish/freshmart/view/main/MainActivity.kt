package com.jagadish.freshmart.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.databinding.ActivityMainBinding
import com.jagadish.freshmart.utils.Singleton
import com.jagadish.freshmart.view.intro.IntroSliderActivity
import com.jagadish.freshmart.view.login.LoginActivity
import com.jagadish.freshmart.view.main.ui.cart.CartFragment
import com.jagadish.freshmart.view.main.ui.location.LocationRequestFragment
import com.jagadish.freshmart.view.main.ui.store.StoreFragment
import com.jagadish.freshmart.view.main.ui.store.model.SelectedAddress
import com.jagadish.freshmart.view.orderinfo.OrderinfoFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val LOCATION_SETTINGS_REQUEST = 199
    private val REQUEST_ENABLE_GPS = 516

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Represents a geographical location.
     */
     var address = MutableLiveData<SelectedAddress>()
    private var mLastLocation: Location? = null

    var currentAddress: LiveData<SelectedAddress> = address
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkPermissions()) {
            startActivity(Intent(this@MainActivity, LocationRequestFragment::class.java))
            finish()
        } else {
            binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

            val navController = findNavController(R.id.nav_host_fragment)

            binding.navView.setupWithNavController(navController)

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getLastLocation()

            updateCart()
            binding.navView.setOnNavigationItemSelectedListener { item ->
             var isNavigate= false
                when (item.itemId) {
                    R.id.navigation_store -> {
                        isNavigate = true
                        val navController = findNavController(R.id.nav_host_fragment)
                        navController.navigate(R.id.navigation_store)
                    }
                    R.id.navigation_cart -> {
                        isNavigate = true
                        val navController = findNavController(R.id.nav_host_fragment)
                        navController.navigate(R.id.navigation_cart)
                    }
                    R.id.navigation_orders -> {
                        if(!SharedPreferencesUtils.getBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN)) {
                            isNavigate = false
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        }else {
                            isNavigate = true
                            val navController = findNavController(R.id.nav_host_fragment)
                            navController.navigate(R.id.navigation_orders)
                        }
                    }
                    R.id.navigation_profile -> {
                        if(!SharedPreferencesUtils.getBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN)) {
                            isNavigate = false
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        }else {
                            isNavigate = true
                            val navController = findNavController(R.id.nav_host_fragment)
                            navController.navigate(R.id.navigation_profile)
                        }
                    }
                }
                isNavigate
            }
        }

    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     *
     *
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result

                    if (mLastLocation!!.latitude != 0.0 && mLastLocation!!.longitude != 0.0) {
                        val gCoder = Geocoder(this)
                        val addresses: ArrayList<Address>? =
                            gCoder.getFromLocation(
                                mLastLocation!!.latitude,
                                mLastLocation!!.longitude,
                                1
                            ) as ArrayList<Address>?
                        if (addresses != null && addresses.size > 0) {
                            address.value = SelectedAddress(addresses[0].featureName,addresses[0].getAddressLine(0),addresses[0].postalCode)
                        }
                    }

                } else {
                }
            }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }


    override fun onStart() {
        super.onStart()
        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval((10 * 1000).toLong())
            .setFastestInterval((1 * 1000).toLong())
        val settingsBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        settingsBuilder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this)
            .checkLocationSettings(settingsBuilder.build())

        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
            } catch (ex: ApiException) {
                when (ex.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException = ex as ResolvableApiException
                        resolvableApiException
                            .startResolutionForResult(
                                this@MainActivity,
                                LOCATION_SETTINGS_REQUEST
                            )
                    } catch (e: SendIntentException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            when (resultCode) {
                RESULT_OK -> {
                    getLastLocation()
                }
                RESULT_CANCELED -> {
                    Log.e("GPS", "User denied to access location")
                    openGpsEnableSetting()
                }
            }
        } else if (requestCode == REQUEST_ENABLE_GPS) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!isGpsEnabled) {
                openGpsEnableSetting()
            } else {
                // implement api call
            }
        }
    }

    private fun openGpsEnableSetting() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, REQUEST_ENABLE_GPS)
    }

    fun clearCartBadge(){
        binding.navView.removeBadge(R.id.navigation_cart)
    }

    fun updateCart(){
        if(Singleton.getInstance().cart != null && Singleton.getInstance().cart?.count != 0) {
            binding.navView.getOrCreateBadge(R.id.navigation_cart).number =
                Singleton.getInstance().cart.count
        }else{
            clearCartBadge()
        }
    }
}