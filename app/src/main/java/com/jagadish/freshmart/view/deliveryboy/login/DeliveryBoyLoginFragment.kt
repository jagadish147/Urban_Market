package com.jagadish.freshmart.view.deliveryboy.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import com.jagadish.freshmart.BuildConfig
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.cart.CreateCartRes
import com.jagadish.freshmart.data.dto.login.RequestOtpReq
import com.jagadish.freshmart.data.dto.login.RequestOtpRes
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.ActivityLoginBinding
import com.jagadish.freshmart.databinding.FragmentDeliveryBoyLoginBinding
import com.jagadish.freshmart.databinding.FragmentLoginBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.deliveryboy.DeliveryHomeActivity
import com.jagadish.freshmart.view.intro.IntroSliderActivity
import com.jagadish.freshmart.view.login.LoginActivity
import com.jagadish.freshmart.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_delivery_boy_login.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.login
import kotlinx.android.synthetic.main.fragment_login.mobile_number

@AndroidEntryPoint
class DeliveryBoyLoginFragment : BaseFragment() {

    companion object {
        fun newInstance() = DeliveryBoyLoginFragment()
    }

    private lateinit var binding: FragmentDeliveryBoyLoginBinding
    private val loginViewModel: DeliveryBoyFragmentLoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeliveryBoyLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel

        observeViewModel()
        login.setOnClickListener {
            if (Validator.isValidPhone(mobile_number.text.toString(),true) && Validator.isValidPassword(password.text.toString(),true))
                loginViewModel.requestDeliveryBoyLogin(RequestOtpReq(mobile_number.text.toString(),password.text.toString(),mobile_number.text.toString(),SharedPreferencesUtils.getAppStringPreference(SharedPreferencesUtils.PREF_APP_FCM_TOKEN)))
        }


    }

    private fun observeViewModel() {
        observe(loginViewModel.recipesLiveData, ::handleRecipesList)
        observeSnackBarMessages(loginViewModel.showSnackBar)
        observeToast(loginViewModel.showToast)

    }

    private fun bindListData(res: RequestOtpRes) {
        if(res.success) {
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
        SharedPreferencesUtils.setBooleanPreference(SharedPreferencesUtils.PREF_USER_LOGIN,true)
        SharedPreferencesUtils.setIntPreference(SharedPreferencesUtils.PREF_USER_ID,res.driver_id)
        SharedPreferencesUtils.setStringPreference(SharedPreferencesUtils.PREF_USER_NAME,res.name)
        SharedPreferencesUtils.setStringPreference(SharedPreferencesUtils.PREF_USER_MOBILE,res.phone_number)
        SharedPreferencesUtils.setStringPreference(SharedPreferencesUtils.PREF_USER_EMAIL,res.email)
        startActivity(Intent(requireActivity(), DeliveryHomeActivity::class.java))
        requireActivity().finish()
    }

}