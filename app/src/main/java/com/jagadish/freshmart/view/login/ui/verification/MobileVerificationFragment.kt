package com.jagadish.freshmart.view.login.ui.verification

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
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
@AndroidEntryPoint
class MobileVerificationFragment : BaseFragment() {

    companion object {
        fun newInstance() = MobileVerificationFragment()
    }

    private lateinit var binding: FragmentMobileVerificationBinding
    private val args: MobileVerificationFragmentArgs by navArgs()
    private val loginViewModel: MobileVerificationViewModel by viewModels()
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

        verify.setOnClickListener {
            val res = args?.requestOtpRes
            if(Validator.isValidOtp(otp_view.otp.toString(),res.customer_otp)) {
                if(res.status == 404){
                    val data = MobileVerificationFragmentDirections.actionNavigationMobileVerificationToNavigationLoginDetails(res)
                    findNavController().navigate(data)
                }else
                    loginViewModel.requestLogin(RequestOtpReq(res.mobileNumber))
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
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }
}