package com.jagadish.freshmart.utils

import android.graphics.Color
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.androidadvance.topsnackbar.TSnackbar
import com.jagadish.freshmart.R
import java.util.regex.Pattern

/**
 * Created by Jagadeesh on 27-02-2021.
 */
class Validator {

    companion object {

        // Default validation messages
        private val NAME_VALIDATION_MSG = "Enter a valid name"
        private val EMAIL_VALIDATION_MSG = "Enter a valid email address"
        private val PHONE_VALIDATION_MSG = "Enter a valid phone number"
        private val PASSWORD_VALIDATION_MSG = "Enter a valid Password"
        private val OTP_VALIDATION_MSG = "Enter a valid OTP"

        /**
         * Retrieve string data from the parameter.
         * @param data - can be EditText or String
         * @return - String extracted from EditText or data if its data type is Strin.
         */
        private fun getText(data: Any): String {
            var str = ""
            if (data is EditText) {
                str = data.text.toString()
            } else if (data is String) {
                str = data
            }
            return str
        }

        /**
         * Checks if the name is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the name is valid.
         */
        fun isValidName(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = str.trim().length > 2

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else NAME_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }

        /**
         * Checks if the name is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the name is valid.
         */
        fun isValidOtp(data: Any, otp: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = str.trim() == otp

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else NAME_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }

        /**
         * Checks if the email is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the email is valid.
         */
        fun isValidEmail(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = Patterns.EMAIL_ADDRESS.matcher(str).matches()

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else EMAIL_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }

        fun isValidPassword(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = str.length >= 4

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else PASSWORD_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }

        /**
         * Checks if the phone is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the phone is valid.
         */
        fun isValidPhone(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = Patterns.PHONE.matcher(str).matches()

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else PHONE_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }


        /**
         * Sets error on EditText or TextInputLayout of the EditText.
         * @param data - Should be EditText
         * @param error - Message to be shown as error, can be null if no error is to be set
         */
        fun setError(data: Any, error: String?) {
            if (data is EditText && !error.isNullOrBlank()) {
                val snackbar = TSnackbar.make(
                    data.rootView.findViewById(android.R.id.content),
                    error.toString(),
                    TSnackbar.LENGTH_LONG
                )
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(ContextCompat.getColor(data.rootView.context, R.color.red))
                val textView =
                    snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                snackbar.show()
            } else if (data is TextView && !error.isNullOrBlank()) {
                val snackbar = TSnackbar.make(
                    data.rootView.findViewById(android.R.id.content),
                    error.toString(),
                    TSnackbar.LENGTH_LONG
                )
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(ContextCompat.getColor(data.rootView.context, R.color.red))
                val textView =
                    snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                snackbar.show()
            }
        }

    }
}