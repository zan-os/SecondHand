package id.co.secondhand.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.remote.request.LoginRequest
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityLoginBinding
import id.co.secondhand.ui.MainActivity
import id.co.secondhand.ui.auth.register.RegisterActivity
import id.co.secondhand.utils.DismissKeyboard.dismissKeyboard
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
        registerUser()
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

    private fun registerUser() {
        validateLogin()

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            val user = LoginRequest(
                email = email,
                password = password
            )

            viewModel.login(user).observe(this) { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d("Market", "Loading")
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        viewModel.saveAccessToken(result.data?.accessToken ?: "")
                        Log.d("Market", result.data.toString())
                        navigateToHomepage()
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Log.d("Market", "Error ${result.message.toString()}")
                        showErrorMessage(result.message, it)
                    }
                }
            }

            window.decorView.clearFocus()
            it.dismissKeyboard()
        }
    }

    private fun showLoading(value: Boolean) {
        if (value) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun showErrorMessage(code: String?, view: View) {
        when (code) {
            "401" -> {
                "User tidak ditemukan".showSnackbar(
                    view = view,
                    context = this,
                    textColor = R.color.white,
                    backgroundColor = R.color.alert_danger
                )
            }
            "500" -> {
                "Internal Server Error :(".showSnackbar(
                    view,
                    this,
                    R.color.white,
                    R.color.alert_danger
                )
            }
            "503" -> {
                "Service Unavailable".showSnackbar(
                    view,
                    this,
                    R.color.white,
                    R.color.alert_danger
                )
            }
        }
    }

    private fun navigateToHomepage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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