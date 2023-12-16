package com.example.weatherapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.weatherapp.common.utils.clearErrorOnTextChanged
import com.example.weatherapp.databinding.FragmentLoginBinding
import com.example.weatherapp.ui.auth.AuthActivity
import com.example.weatherapp.ui.home.MainActivity
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setObservers()
        handleClickActions(view)
    }

    private fun init() {
        binding.emailEditText.clearErrorOnTextChanged(binding.emailTextInputLayout)
        binding.passwordEditText.clearErrorOnTextChanged(binding.passwordTextInputLayout)
    }

    private fun setObservers() {
        loginViewModel.emailError.observe(this) {
            handleError(textInputLayout = binding.emailTextInputLayout, stringResId = it)
        }
        loginViewModel.passwordError.observe(this) {
            handleError(textInputLayout = binding.passwordTextInputLayout, stringResId = it)
        }
        loginViewModel.currentLoginUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                resetForm()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                (this.activity as? AuthActivity)?.finish()
            }
        }
    }

    private fun handleClickActions(view: View) {

        binding.loginButton.setOnClickListener {
            val email: String = binding.emailEditText.text.toString()
            val password: String = binding.passwordEditText.text.toString()
            loginViewModel.loginUser(email = email, password = password)
        }

        binding.tvSignup.setOnClickListener {
            resetForm()
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            it.findNavController().navigate(action)
        }
    }

    private fun handleError(textInputLayout: TextInputLayout, stringResId: Int?) {
        if (stringResId == null) {
            textInputLayout.error = null
        } else {
            textInputLayout.error = resources.getString(stringResId)
            textInputLayout.editText?.requestFocus()
        }
    }

    private fun resetForm() {
        loginViewModel.resetErrors()
        binding.emailEditText.text = null
        binding.passwordEditText.text = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}