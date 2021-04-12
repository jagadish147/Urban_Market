package com.jagadish.freshmart.view.login.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.CreateCartRes
import com.jagadish.freshmart.data.dto.login.RequestOtpReq
import com.jagadish.freshmart.data.dto.login.RequestOtpRes
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.ActivityLoginBinding
import com.jagadish.freshmart.databinding.FragmentLoginBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.intro.IntroSliderActivity
import com.jagadish.freshmart.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var requestOtpRes: RequestOtpRes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        observeViewModel()
        login.setOnClickListener {
            if(Validator.isValidPhone(mobile_number.text.toString()))
                loginViewModel.requestOtp(RequestOtpReq(mobile_number.text.toString(),"","",SharedPreferencesUtils.getAppStringPreference(SharedPreferencesUtils.PREF_APP_FCM_TOKEN)))
        }

        initilizeCallBack()
    }

    private fun observeViewModel() {
        observe(loginViewModel.recipesLiveData, ::handleRecipesList)
        observeSnackBarMessages(loginViewModel.showSnackBar)
        observeToast(loginViewModel.showToast)

    }

    private fun bindListData(res: RequestOtpRes) {
        if(res.success && res.status == 200) {
            navigateHome(res)
        }else if(!res.success && res.status == 404) {
            navigateHome(res)
        }else{
            Validator.setError(binding.mobileNumber,res.message)
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
        binding.pbLoading.toVisible()
    }


    private fun hideLoadingView() {
        binding.pbLoading.toGone()
    }



    private fun handleRecipesList(status: Resource<RequestOtpRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(it) }
            is Resource.DataError -> {
                status.errorCode?.let { loginViewModel.showToastMessage(it) }
            }
        }
    }

    private fun navigateHome(res: RequestOtpRes){
        requestOtpRes = res
        requestOtpRes.mobileNumber = binding.mobileNumber.text.toString()
        sendVerificationCode(binding.mobileNumber.text.toString())

    }
    // create instance of firebase auth
    lateinit var auth: FirebaseAuth
    // we will use this to match the sent otp from firebase
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private fun initilizeCallBack(){
        auth= FirebaseAuth.getInstance()
        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                startActivity(Intent(applicationContext, MainActivity::class.java))
//                finish()
                Log.d("GFG" , "onVerificationCompleted Success")
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG" , "onVerificationFailed  $e")
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Validator.setError(binding.mobileNumber,"Invalid phone number.")
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Validator.setError(binding.mobileNumber,"Quota exceeded.")
                }
            }

            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG","onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token
                // Start a new activity using intent
                // also send the storedVerificationId using intent
                // we will use this id to send the otp back to firebase
                hideLoadingView()
                requestOtpRes.fcmOtp = verificationId
                requestOtpRes.fcmResendToken = token
                val bundle = LoginFragmentDirections.actionNavigationLoginToNavigationMobileVerification(requestOtpRes)
                findNavController().navigate(bundle)
//                val intent = Intent(applicationContext,OtpActivity::class.java)
//                intent.putExtra("storedVerificationId",storedVerificationId)
//                startActivity(intent)
//                finish()
            }
        }
    }
    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91"+number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}