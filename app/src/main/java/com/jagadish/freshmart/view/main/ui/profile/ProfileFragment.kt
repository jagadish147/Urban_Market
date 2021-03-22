package com.jagadish.freshmart.view.main.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.databinding.FragmentHomeBinding
import com.jagadish.freshmart.databinding.FragmentProfileBinding
import com.jagadish.freshmart.view.address.AddressActivity
import com.jagadish.freshmart.view.main.ui.orders.OrdersViewModel
import com.jagadish.freshmart.view.orderinfo.OrderInfoActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileName.text = SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_NAME)
        binding.profileEmail.text = "| "+SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_EMAIL)
        binding.profileMobile.text = SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_MOBILE)

        binding.addressText.setOnClickListener {
            val nextScreenIntent = Intent(requireActivity(), AddressActivity::class.java).apply {
//                putExtra(CATEGORY_KEY, it)
            }
            startActivity(nextScreenIntent)
        }
        binding.referText.setOnClickListener {
            ShareCompat.IntentBuilder.from(requireActivity())
                .setType("text/plain")
                .setChooserTitle("Share App")
                .setText("http://play.google.com/store/apps/details?id=" + requireActivity().getPackageName())
                .startChooser();
        }
        binding.logoutText.setOnClickListener { SharedPreferencesUtils.clearAllPreferences()
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_store)
        }

    }
}