package com.jagadish.freshmart.view.login.ui.verification

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.login.CustomersRequest
import com.jagadish.freshmart.data.dto.login.CustomersRes
import com.jagadish.freshmart.data.dto.login.RequestOtpReq
import com.jagadish.freshmart.data.dto.login.RequestOtpRes
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentLoginBinding
import com.jagadish.freshmart.databinding.FragmentMobileVerificationBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.login.ui.details.LoginDetailsFragmentArgs
import com.jagadish.freshmart.view.login.ui.login.LoginFragmentDirections
import com.jagadish.freshmart.view.login.ui.login.LoginViewModel
import com.jagadish.freshmart.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_mobile_verification.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MobileVerificationFragment : BaseFragment() {

    companion object {
        fun newInstance() = MobileVerificationFragment()
    }

    private lateinit var binding: FragmentMobileVerificationBinding
    private val args: MobileVerificationFragmentArgs by navArgs()
    private val loginViewModel: MobileVerificationViewModel by viewModels()
    // get reference of the firebase auth
    lateinit var auth: FirebaseAuth
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var res : RequestOtpRes
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMobileVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
         res = args?.requestOtpRes
        binding.descTxt.text= "We sent a verification code to "+res.mobileNumber+" please enter verification code below"
        resendOTPTimer()
        binding.resendTxt.setOnClickListener {
            resendVerificationCode(res.mobileNumber,res.fcmResendToken)
        }
        verify.setOnClickListener {
            val otp = otp_view.otp.toString()
            if(otp.isNotEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    res.fcmOtp, otp)
                signInWithPhoneAuthCredential(credential)
            }

//            val res = args?.requestOtpRes
//            if(Validator.isValidOtp(otp_view.otp.toString(),res.customer_otp)) {
//                if(res.status == 404){
//                    val data = MobileVerificationFragmentDirections.actionNavigationMobileVerificationToNavigationLoginDetails(res)
//                    findNavController().navigate(data)
//                }else
//                    loginViewModel.requestLogin(RequestOtpReq(res.mobileNumber,"","",SharedPreferencesUtils.getAppStringPreference(SharedPreferencesUtils.PREF_APP_FCM_TOKEN),SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID)))
//            }
        }

        auth=FirebaseAuth.getInstance()

        // get storedVerificationId from the intent
        val storedVerificationId= args?.requestOtpRes

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // verification completed

            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked if an invalid request for verification is made,
                // for instance if the the phone number format is invalid.

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Validator.setError(binding.descTxt,"Invalid phone number.")
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Validator.setError(binding.descTxt,"Quota exceeded.")
                }

            }
            override fun onCodeSent(verificationId: String?,
                                    token: PhoneAuthProvider.ForceResendingToken?) {
                resendOTPTimer()
                if (verificationId != null) {
                    res.fcmOtp = verificationId
                }
                if (token != null) {
                    res.fcmResendToken = token
                }
            }
            override fun onCodeAutoRetrievalTimeOut(verificationId: String?) {
                // called when the timeout duration has passed without triggering onVerificationCompleted
                super.onCodeAutoRetrievalTimeOut(verificationId)
            }
        }

    }

    private fun observeViewModel() {
        observe(loginViewModel.recipesLiveData, ::handleRecipesList)
        observeSnackBarMessages(loginViewModel.showSnackBar)
        observeToast(loginViewModel.showToast)

    }

    private fun bindListData(res: CustomersRes) {
        if(res.success) {
            navigateHome(res)
        }
    }


    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun showSearchError() {
        loginViewModel.showToastMessage(SEARCH_ERROR)
    }



    private fun showLoadingView() {
        binding.loading.toVisible()
    }



    private fun handleRecipesList(status: Resource<CustomersRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(it) }
            is Resource.DataError -> {
                status.errorCode?.let { loginViewModel.showToastMessage(it) }
            }
        }
    }

    private fun navigateHome(res: CustomersRes){
        SharedPreferencesUtils.setBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN,true)
        SharedPreferencesUtils.setIntPreference(SharedPreferencesUtils.PREF_USER_ID,res.customer.id)
        SharedPreferencesUtils.setStringPreference(SharedPreferencesUtils.PREF_USER_NAME,res.customer.name)
        SharedPreferencesUtils.setStringPreference(SharedPreferencesUtils.PREF_USER_MOBILE,args.requestOtpRes.mobileNumber)
        SharedPreferencesUtils.setStringPreference(SharedPreferencesUtils.PREF_USER_EMAIL,res.customer.email)
        SharedPreferencesUtils.setStringPreference(SharedPreferencesUtils.PREF_DEVICE_CART, res.cart_id.toString())
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }


    // verifies if the code matches sent by firebase
    // if success start the new activity in our case it is main Activity
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if(res.status == 404){
                        val data = MobileVerificationFragmentDirections.actionNavigationMobileVerificationToNavigationLoginDetails(res)
                        findNavController().navigate(data)
                    }else
                        loginViewModel.requestLogin(RequestOtpReq(res.mobileNumber,"","",SharedPreferencesUtils.getAppStringPreference(SharedPreferencesUtils.PREF_APP_FCM_TOKEN),SharedPreferencesUtils.getIntPreference(SharedPreferencesUtils.PREF_DEVICE_CART_ID)))
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(requireContext(),"Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun resendOTPTimer(){
        binding.loading.toGone()
        binding.resendTxt.isClickable = false
        val timer = object: CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTextUI(millisUntilFinished)

            }

            override fun onFinish() {
                binding.resendTxt.text = "Resend OTP"
                binding.resendTxt.isClickable = true
            }
        }
        timer.start()
    }
    private fun updateTextUI(time_in_milli_seconds : Long) {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60

        binding.resendTxt.text  = "$minute:$seconds"
    }

    private fun resendVerificationCode(phoneNumber: String,
                                       token: PhoneAuthProvider.ForceResendingToken?) {
        binding.loading.toVisible()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91"+phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(mCallbacks)
            .setForceResendingToken(token)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}