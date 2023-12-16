package com.example.weatherapp.common.validation

import androidx.core.util.PatternsCompat
import com.example.weatherapp.R

object ValidationHelper {

    /**
     * Email Validation Function
     * @param input value in string.
     * @return  returns ValidationResults(isValid:Boolean, error:String?)
     * @see ValidationResults
     */
    fun validateEmail(input: String): ValidationResults {
        val textInput = input.trim()
        textInput.let { email ->
            if (email.isEmpty()) {
                return ValidationResults(isValid = false, error = R.string.error_field_required)
            } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                return ValidationResults(isValid = false, error = R.string.error_invalid_email)
            } else {
                return ValidationResults(isValid = true)
            }
        }
    }

    /**
     * Password Validation
     * @param input value in string.
     * @return  returns ValidationResults(isValid:Boolean, error:String?)
     * @see ValidationResults
     */
    fun validatePassword(input: String): ValidationResults {
        val trimmedInputText = input.trim()
        trimmedInputText.let { password ->
            if (password.isEmpty()) {
                return ValidationResults(isValid = false, error = R.string.error_field_required)
            } else if (password.length < 6) {
                return ValidationResults(
                    isValid = false,
                    error = R.string.error_weak_password
                )
            } else if (!password.contains(Regex("\\d"))) {
                return ValidationResults(
                    isValid = false,
                    error = R.string.error_must_contain_digit
                )
            } else if (!password.contains(Regex("[A-Z]"))) {
                return ValidationResults(
                    isValid = false,
                    error = R.string.error_must_contain_capital_letter
                )
            } else if (!password.contains(Regex("[^a-zA-Z\\d ]"))) {
                return ValidationResults(
                    isValid = false,
                    error = R.string.error_must_contain_spacial_char
                )
            } else if (password.contains(Regex(" "))) {
                return ValidationResults(
                    isValid = false,
                    error = R.string.error_contain_white_space
                )
            } else {
                return ValidationResults(isValid = true)
            }
        }
    }

    /**
     * UserName Field Validation
     * @param input value in string.
     * @return  returns ValidationResults(isValid:Boolean, error:String?)
     * @see ValidationResults
     */
    fun validateUserName(input: String): ValidationResults {
        val textInput = input.trim()
        textInput.let { username ->
            if (username.isEmpty()) {
                return ValidationResults(
                    isValid = false,
                    error = R.string.error_field_required
                )
            } else if (username.length <= 4) {
                return ValidationResults(
                    isValid = false,
                    error = R.string.error_five_char_long
                )
            } else if (username.matches(Regex("^\\d.*"))) {
                return ValidationResults(
                    isValid = false,
                    error = R.string.error_start_with_number
                )
            } else if (username.contains(" ")) {
                return ValidationResults(
                    isValid = false,
                    error = R.string.error_contain_white_space
                )
            } else {
                return ValidationResults(isValid = true)
            }
        }
    }

    /**
     * RepeatedPassword Validation
     * @param pass value in string.
     * @param repeatedPass value in string
     * @return  returns ValidationResults(isValid:Boolean, error:String?)
     * @see ValidationResults
     */
    fun validateRepeatedPassword(pass: String, repeatedPass: String): ValidationResults {
        val passText = pass.trim()
        val confirmPassText = repeatedPass.trim()
        return if (passText.isEmpty()) {
            ValidationResults(isValid = false, error = R.string.error_enter_pass_first)
        } else if (confirmPassText.isEmpty()) {
            ValidationResults(isValid = false, error = R.string.error_pass_can_not_be_blank)
        } else if (passText == confirmPassText) {
            ValidationResults(
                isValid = true
            )
        } else {
            ValidationResults(
                isValid = false, error = R.string.error_pass_not_matched
            )
        }
    }

}