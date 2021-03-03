package com.jagadish.freshmart.view.login.ui.verification

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jagadish.freshmart.R
import com.jagadish.freshmart.utils.Validator
import kotlinx.android.synthetic.main.fragment_mobile_verification.*

class MobileVerificationFragment : Fragment() {

    companion object {
        fun newInstance() = MobileVerificationFragment()
    }

    private lateinit var viewModel: MobileVerificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mobile_verification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MobileVerificationViewModel::class.java)
        // TODO: Use the ViewModel

        verify.setOnClickListener {
            if(Validator.isValidOtp(otp_view.otp.toString(),""))
                activity?.finish()
        }
    }

}