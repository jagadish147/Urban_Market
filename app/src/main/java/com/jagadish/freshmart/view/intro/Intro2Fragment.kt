package com.jagadish.freshmart.view.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jagadish.freshmart.R

class Intro2Fragment : Fragment() {
   override fun onCreateView(
       inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
   ): View? {
       return inflater.inflate(R.layout.fragment_intro2, container, false)
   }
}