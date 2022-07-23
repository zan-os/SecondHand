package id.co.secondhand.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityLoginBinding
import id.co.secondhand.ui.auth.register.RegisterActivity
import id.co.secondhand.ui.main.MainActivity
import id.co.secondhand.utils.Extension.dismissKeyboard
import id.co.secondhand.utils.Extension.showSnackbar
import id.co.secondhand.utils.Extension.validateEmail
import id.co.secondhand.utils.Extension.validatePassword

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        login()
        navigateToRegister()
    }

    private fun validateLogin() {
        binding.apply {
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    val email = emailEt.text.toString()
                    val password = passwordEt.text.toString()
                    loginBtn.isEnabled = email.validateEmail() && password.validatePassword()
                }
            }
            emailEt.addTextChangedListener(textWatcher)
            passwordEt.addTextChangedListener(textWatcher)
        }
    }

    private fun login() {
        validateLogin()

        binding.loginBtn.setOnClickListener { it ->
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            val loginRequest = LoginRequest(
                email = email,
                password = password
            )

            viewModel.login(loginRequest).observe(this) { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        viewModel.saveAccessToken(result.data?.accessToken ?: "")
                        navigateToHome()
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        result.message?.let { showErrorMessage(it) }
                    }
                }
            }

            window.decorView.clearFocus()
            it.dismissKeyboard()
        }
    }

    private fun showLoading(visible: Boolean) {
        binding.progressCircular.isVisible = visible
    }

    private fun showErrorMessage(message: String) {
        when (message) {
            "401" -> {
                getString(R.string.error_email_password).showSnackbar(
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

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToRegister() {
        binding.toRegisterBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            val direction = Intent(this, RegisterActivity::class.java)
            startActivity(direction)
            finish()
        }
    }
}