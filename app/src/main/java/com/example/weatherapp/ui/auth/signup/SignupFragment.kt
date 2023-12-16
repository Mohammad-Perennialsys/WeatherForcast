package com.example.weatherapp.ui.auth.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.common.utils.clearErrorOnTextChanged
import com.example.weatherapp.databinding.FragmentSignupBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding: FragmentSignupBinding get() = _binding!!
    private val signupViewModel: SignupViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setObservers()
        handleUIActions()
    }

    private fun init() {
        binding.usernameEditText.clearErrorOnTextChanged(binding.usernameTextInputLayout)
        binding.emailEditText.clearErrorOnTextChanged(binding.emailTextInputLayout)
        binding.passwordEditText.clearErrorOnTextChanged(binding.passwordTextInputLayout)
        binding.confirmPasswordEditText.clearErrorOnTextChanged(binding.confirmPasswordTextInputLayout)
    }

    private fun setObservers() {
        signupViewModel.usernameError.observe(this) {
            handleError(binding.usernameTextInputLayout, it)
        }
        signupViewModel.emailError.observe(this) {
            handleError(binding.emailTextInputLayout, it)
        }
        signupViewModel.passwordError.observe(this) {
            handleError(binding.passwordTextInputLayout, it)
        }
        signupViewModel.confirmPasswordError.observe(this) {
            handleError(binding.confirmPasswordTextInputLayout, it)
        }
        signupViewModel.isSignupSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                resetForm()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.create_user_success_message),
                    Toast.LENGTH_SHORT
                ).show()
                this.requireView().findNavController().popBackStack()
            }
        }
    }

    private fun handleError(textInputLayout: TextInputLayout, stringResId: Int?) {
        stringResId?.let {
            textInputLayout.error = resources.getString(it)
        }
    }

    private fun handleUIActions() {
        binding.signUpButton.setOnClickListener {
            val userName: String = binding.usernameEditText.text.toString()
            val email: String = binding.emailEditText.text.toString()
            val password: String = binding.passwordEditText.text.toString()
            val confirmPassword: String = binding.confirmPasswordEditText.text.toString()
            signupViewModel.signupUser(
                userName = userName,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )
        }

        binding.tvLogin.setOnClickListener {
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            it.findNavController().navigate(action)
        }
    }

    private fun resetForm() {
        signupViewModel.resetErrors()
        binding.usernameEditText.text = null
        binding.emailEditText.text = null
        binding.passwordEditText.text = null
        binding.confirmPasswordEditText.text = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}