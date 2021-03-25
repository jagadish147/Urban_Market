package com.jagadish.freshmart.view.address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.Resource
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.address.AddAddressRes
import com.jagadish.freshmart.data.dto.login.RequestOtpRes
import com.jagadish.freshmart.data.error.SEARCH_ERROR
import com.jagadish.freshmart.databinding.FragmentAddAddressBinding
import com.jagadish.freshmart.databinding.FragmentAddressListBinding
import com.jagadish.freshmart.utils.*
import com.jagadish.freshmart.view.login.ui.details.LoginDetailsFragmentArgs
import com.jagadish.freshmart.view.login.ui.login.LoginFragmentDirections
import com.jagadish.freshmart.view.login.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class AddAddressFragment : BaseFragment() {

    private lateinit var binding: FragmentAddAddressBinding
    private val loginViewModel: AdressViewModel by viewModels()
    private val args: AddAddressFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAddressBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        if(args.requestAddress.id !=0){
            binding.address = args.requestAddress
            binding.buttonSave.text = "Update Address"
        }
        binding.buttonSave.setOnClickListener {
            if(args.requestAddress.id !=0){

                loginViewModel.updateAddress(AddAddressReq(
                    args.requestAddress.id ,
                    SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_MOBILE),
                    binding.addressLine1.text.toString().trim(),
                    binding.addressLine2.text.toString().trim(),
                    binding.city.text.toString().trim(),
                    binding.state.text.toString().trim(),
                    binding.zip.text.toString().trim(),
                    binding.defaultCheckBox.isChecked,
                    if(binding.defaultCheckBox.isChecked)"default" else "other",
                ))
            }else {
                loginViewModel.requestAddAddress(
                    AddAddressReq(
                        SharedPreferencesUtils.getIntPreference(
                            SharedPreferencesUtils.PREF_USER_ID
                        ),
                        SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_MOBILE),
                        binding.addressLine1.text.toString().trim(),
                        binding.addressLine2.text.toString().trim(),
                        binding.city.text.toString().trim(),
                        binding.state.text.toString().trim(),
                        binding.zip.text.toString().trim(),
                        binding.defaultCheckBox.isChecked,
                        if(binding.defaultCheckBox.isChecked)"default" else "other",
                    )
                )
            }
        }
    }

//    private fun validateForm(): Boolean {
//        if()
//    }


    private fun observeViewModel() {
        observe(loginViewModel.recipesLiveData, ::handleRecipesList)
        observe(loginViewModel.updateAddress, ::handleUpdateAddress)
        observeSnackBarMessages(loginViewModel.showSnackBar)
        observeToast(loginViewModel.showToast)

    }

    private fun bindListData(res: AddAddressRes) {
        binding.pbLoading.toGone()
        navigateHome(res)
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



    private fun handleRecipesList(status: Resource<AddAddressRes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(it) }
            is Resource.DataError -> {
                status.errorCode?.let { loginViewModel.showToastMessage(it) }
            }
        }
    }

    private fun navigateHome(res: AddAddressRes){
        findNavController().navigate(R.id.action_navigation_address_add_to_navigation_address_list)
    }

    private fun handleUpdateAddress(status: Resource<AddAddressRes>){
        when (status){
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { updateAddressSuccess() }
            is Resource.DataError -> {
                status.errorCode?.let { loginViewModel.showToastMessage(it) }
            }
        }
    }

    private fun updateAddressSuccess(){
        findNavController().navigate(R.id.action_navigation_address_add_to_navigation_address_list)
    }
}