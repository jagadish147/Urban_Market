package com.jagadish.freshmart.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.view.intro.IntroSliderActivity
import com.jagadish.freshmart.view.login.LoginActivity
import com.jagadish.freshmart.view.main.MainActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferencesUtils.init(this)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            if(!SharedPreferencesUtils.getBooleanPreference(SharedPreferencesUtils.PREF_APP_FIRST_TIME)){
                startActivity(Intent(this@SplashActivity, IntroSliderActivity::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        }, 2000)
    }
}