package id.co.secondhand.ui.auth.register

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
import id.co.secondhand.data.remote.request.RegisterRequest
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityRegisterBinding
import id.co.secondhand.ui.auth.login.LoginActivity
import id.co.secondhand.utils.Extension.showSnackbar
import id.co.secondhand.utils.Extension.validateEmail
import id.co.secondhand.utils.Extension.validatePassword

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        validateRegister()
        registerUser()
        navigateToLogin()
    }

    private fun validateRegister() {
        binding.apply {
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val photoProfile = photoProfileIv.toString()
                    val name = nameEt.text.toString()
                    val email = emailEt.text.toString()
                    val password = passwordEt.text.toString()
                    val numberPhone = numberPhoneEt.text.toString()
                    val address = addressEt.text.toString()

                    if (email.validateEmail()) {
                        emailEtLayout.isErrorEnabled = false
                    }

                    if (password.validatePassword()) {
                        passwordEtLayout.isErrorEnabled = false
                    }

                    registerBtn.isEnabled = name.isNotEmpty() &&
                            email.isNotEmpty() &&
                            password.isNotEmpty() &&
                            numberPhone.isNotEmpty() &&
                            address.isNotEmpty()
                }
            }
            nameEt.addTextChangedListener(textWatcher)
            emailEt.addTextChangedListener(textWatcher)
            passwordEt.addTextChangedListener(textWatcher)
            numberPhoneEt.addTextChangedListener(textWatcher)
            addressEt.addTextChangedListener(textWatcher)
        }
    }

    private fun registerUser() {
        binding.registerBtn.setOnClickListener { view ->
            val name = binding.nameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            val user = RegisterRequest(
                fullName = name,
                email = email,
                password = password
            )

            when {
                !email.validateEmail() -> {
                    binding.emailEtLayout.error = resources.getString(R.string.email_tidak_valid)
                }
                !password.validatePassword() -> {
                    binding.passwordEtLayout.error = resources.getString(R.string.min_6_karakter)
                }
                else -> {
                    viewModel.register(user).observe(this) { result ->
                        when (result) {
                            is Resource.Loading -> {
                                showLoading(true)
                                Log.d("Market", "Loading")
                            }
                            is Resource.Success -> {
                                showLoading(false)
                                Log.d("Market", result.data.toString())
                                "Berhasil mendaftar! Silahkan login".showSnackbar(
                                    binding.root,
                                    this,
                                    R.color.white,
                                    R.color.alert_success
                                )
                                val direction = Intent(this, LoginActivity::class.java)
                                startActivity(direction)
                                finish()
                            }
                            is Resource.Error -> {
                                showLoading(false)
                                Log.d("Market", "Error ${result.message}")
                                showErrorMessage(code = result.message, view = view)
                            }
                        }
                    }
                }
            }
            window.decorView.clearFocus()
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
            "400" -> {
                binding.emailEt.error = "Email sudah terdaftar"
                "Email sudah terdaftar".showSnackbar(
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
                "Service Unavaiable".showSnackbar(
                    view,
                    this,
                    R.color.white,
                    R.color.alert_danger
                )
            }
        }
    }

    private fun navigateToLogin() {
        binding.toLoginBtn.setOnClickListener {
            onBackPressed()
        }
    }
}