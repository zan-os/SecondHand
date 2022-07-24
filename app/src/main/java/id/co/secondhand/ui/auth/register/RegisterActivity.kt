package id.co.secondhand.ui.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.remote.request.auth.RegisterRequest
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityRegisterBinding
import id.co.secondhand.utils.Extension.dismissKeyboard
import id.co.secondhand.utils.Extension.showSnackbar
import id.co.secondhand.utils.Extension.validateEmail
import id.co.secondhand.utils.Extension.validatePassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerUser()
        navigateToLogin()
    }

    private fun validateRegister() {
        binding.apply {
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val name = nameEt.text.toString()
                    val email = emailEt.text.toString()
                    val password = passwordEt.text.toString()

                    if (email.validateEmail()) {
                        emailEtLayout.isErrorEnabled = false
                    }

                    if (password.validatePassword()) {
                        passwordEtLayout.isErrorEnabled = false
                    }

                    registerBtn.isEnabled = name.isNotEmpty() &&
                            email.isNotEmpty() &&
                            password.isNotEmpty()
                }
            }
            nameEt.addTextChangedListener(textWatcher)
            emailEt.addTextChangedListener(textWatcher)
            passwordEt.addTextChangedListener(textWatcher)
        }
    }

    private fun registerUser() {
        validateRegister()

        binding.apply {
            registerBtn.setOnClickListener { it ->
                window.decorView.clearFocus()
                it.dismissKeyboard()
                val name = nameEt.text.toString()
                val email = emailEt.text.toString()
                val password = passwordEt.text.toString()

                val registerRequest = RegisterRequest(
                    fullName = name,
                    email = email,
                    password = password
                )

                when {
                    !email.validateEmail() -> {
                        emailEtLayout.error = getString(R.string.email_tidak_valid)
                    }
                    !password.validatePassword() -> {
                        passwordEtLayout.error = getString(R.string.min_6_karakter)
                    }
                    else -> {
                        viewModel.register(registerRequest)
                            .observe(this@RegisterActivity) { result ->
                                when (result) {
                                    is Resource.Loading -> {
                                        showLoading(true)
                                    }
                                    is Resource.Success -> {
                                        showLoading(false)
                                        showSuccessMessage()
                                        lifecycleScope.launch {
                                            delay(1000)
                                            onBackPressed()
                                        }
                                    }
                                    is Resource.Error -> {
                                        showLoading(false)
                                        result.message?.let { showErrorMessage(it) }
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    private fun showSuccessMessage() {
        getString(R.string.success_register).showSnackbar(
            view = binding.root,
            context = this,
            textColor = R.color.white,
            backgroundColor = R.color.alert_success
        )
    }

    private fun showLoading(visible: Boolean) {
        binding.progressCircular.isVisible = visible
    }

    private fun showErrorMessage(message: String) {
        when (message) {
            "400" -> {
                binding.emailEtLayout.error = getString(R.string.error_email_already_exist)
                getString(R.string.error_email_already_exist).showSnackbar(
                    view = binding.root,
                    context = this,
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
            "500" -> {
                getString(R.string.error_internal_server).showSnackbar(
                    view = binding.root,
                    context = this,
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
            else -> {
                message.showSnackbar(
                    view = binding.root,
                    context = this,
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
        }
    }

    private fun navigateToLogin() {
        binding.toLoginBtn.setOnClickListener { onBackPressed() }
    }
}