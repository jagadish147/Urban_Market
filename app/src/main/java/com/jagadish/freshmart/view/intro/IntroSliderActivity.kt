package com.jagadish.freshmart.view.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseActivity
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_intro_slider.*

class IntroSliderActivity : BaseActivity() {
    private val fragmentList = ArrayList<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slider)
        val adapter = IntroSliderAdapter(this)
        vpIntroSlider.adapter = adapter
        fragmentList.addAll(
            listOf(
                Intro1Fragment(), Intro2Fragment(), Intro3Fragment()
            )
        )
        adapter.setFragmentList(fragmentList)
        indicatorLayout.setIndicatorCount(adapter.itemCount)
        indicatorLayout.selectCurrentPosition(0)
        registerListeners()
    }

    private fun registerListeners() {
        vpIntroSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                indicatorLayout.selectCurrentPosition(position)
                if (position < fragmentList.lastIndex) {
                    tvSkip.visibility = View.VISIBLE
                    tvNext.text = resources.getString(R.string.label_next)
                } else {
                    tvSkip.visibility = View.GONE
                    tvNext.text = resources.getString(R.string.label_get_start)
                }
            }
        })
        tvSkip.setOnClickListener {
            SharedPreferencesUtils.setAppBooleanPreference(SharedPreferencesUtils.PREF_APP_FIRST_TIME,true)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        tvNext.setOnClickListener {
            val position = vpIntroSlider.currentItem
            if (position < fragmentList.lastIndex) {
                vpIntroSlider.currentItem = position + 1
            } else {
                SharedPreferencesUtils.setAppBooleanPreference(SharedPreferencesUtils.PREF_APP_FIRST_TIME,true)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}