package com.example.weatherapp.common.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Function to Clear Error Message on text change in Input EditTextField
 * @param inputLayout material component inputLayout
 */
fun TextInputEditText.clearErrorOnTextChanged(inputLayout: TextInputLayout) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // implementation not Needed
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // implementation not Needed
        }
        override fun afterTextChanged(s: Editable?) { inputLayout.error = null }
    })
}