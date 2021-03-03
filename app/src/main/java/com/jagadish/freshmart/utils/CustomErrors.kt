package com.jagadish.freshmart.utils

import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.androidadvance.topsnackbar.TSnackbar
import com.jagadish.freshmart.R

/**
 * Created by Jagadeesh on 27-02-2021.
 */
class CustomErrors{
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
        }
    }
}