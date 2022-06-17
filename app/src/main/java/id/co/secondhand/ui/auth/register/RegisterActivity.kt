package id.co.secondhand.ui.auth.register

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.secondhand.R
import id.co.secondhand.data.remote.request.RegisterRequest
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityRegisterBinding
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
        registerUser()
        navigateToLogin()
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
                    binding.emailEt.error = "Email tidak valid"
                }
                !password.validatePassword() -> {
                    binding.passwordEt.error = "min 6 karakter"
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