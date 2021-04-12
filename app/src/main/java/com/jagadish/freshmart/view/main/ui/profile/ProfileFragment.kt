package com.jagadish.freshmart.view.main.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jagadish.freshmart.BuildConfig
import com.jagadish.freshmart.IS_COME_PROFILE
import com.jagadish.freshmart.R
import com.jagadish.freshmart.base.BaseFragment
import com.jagadish.freshmart.data.SharedPreferencesUtils
import com.jagadish.freshmart.databinding.FragmentHomeBinding
import com.jagadish.freshmart.databinding.FragmentProfileBinding
import com.jagadish.freshmart.view.address.AddressActivity
import com.jagadish.freshmart.view.deliveryboy.login.DeliveryBoyLoginActivity
import com.jagadish.freshmart.view.main.MainActivity
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
        val window = requireActivity()!!.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            com.jagadish.freshmart.R.color.main_color
        )
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileName.text = SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_NAME)
        binding.profileEmail.text = "| "+SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_EMAIL)
        binding.profileMobile.text = SharedPreferencesUtils.getStringPreference(SharedPreferencesUtils.PREF_USER_MOBILE)

        if(BuildConfig.FLAVOR != "user"){
            binding.addressLabel.text = "Delivery Reports"
        }
        binding.addressText.setOnClickListener {
            if(BuildConfig.FLAVOR != "user") {
                val nextScreenIntent =
                    Intent(requireActivity(), OrderInfoActivity::class.java).apply {
                    putExtra(IS_COME_PROFILE, true)
                    }
                startActivity(nextScreenIntent)
            }else{
                val nextScreenIntent =
                    Intent(requireActivity(), AddressActivity::class.java).apply {
//                putExtra(CATEGORY_KEY, it)
                    }
                startActivity(nextScreenIntent)
            }
        }
        binding.referText.setOnClickListener {
            ShareCompat.IntentBuilder.from(requireActivity())
                .setType("text/plain")
                .setChooserTitle("Share App")
                .setText("http://play.google.com/store/apps/details?id=" + requireActivity().getPackageName())
                .startChooser();
        }
        binding.logoutText.setOnClickListener {
            SharedPreferencesUtils.clearAllPreferences()
            if(BuildConfig.FLAVOR == "user") {
                val isUserSignedIn = FirebaseAuth.getInstance().currentUser != null
                if (isUserSignedIn) signOut()

            }else{
                startActivity(Intent(requireActivity(), DeliveryBoyLoginActivity::class.java))
                requireActivity().finish()
            }
        }



    }

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
        (activity as MainActivity).clearCartBadge()
        findNavController().navigate(R.id.action_navigation_profile_to_navigation_store)
    }
}