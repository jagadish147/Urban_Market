package com.jagadish.freshmart.view.login.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jagadish.freshmart.R
import com.jagadish.freshmart.utils.Validator
import kotlinx.android.synthetic.main.fragment_login_details.*

class LoginDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = LoginDetailsFragment()
    }

    private lateinit var viewModel: LoginDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginDetailsViewModel::class.java)
        // TODO: Use the ViewModel

        next.setOnClickListener {
            if(Validator.isValidName(username.text.toString()) && Validator.isValidEmail(email.text.toString()))
                findNavController().navigate(R.id.action_navigation_login_details_to_navigation_mobile_verification)
        }
    }

}