package id.co.secondhand.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
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
        registerUser()
        navigateToLogin()
    }

    private fun validateRegister() {
        binding.apply {
            imagePicker()
            dropDownMenu()
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val name = nameEt.text.toString()
                    val email = emailEt.text.toString()
                    val password = passwordEt.text.toString()
                    val numberPhone = numberPhoneEt.text.toString()
                    val city = autoCompleteCityTv.text.toString()
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
                            city.isNotEmpty() &&
                            address.isNotEmpty()
                }
            }
            nameEt.addTextChangedListener(textWatcher)
            emailEt.addTextChangedListener(textWatcher)
            passwordEt.addTextChangedListener(textWatcher)
            numberPhoneEt.addTextChangedListener(textWatcher)
            autoCompleteCityTv.addTextChangedListener(textWatcher)
            addressEt.addTextChangedListener(textWatcher)
            addressEt.setRawInputType(InputType.TYPE_CLASS_TEXT)
        }
    }

    private fun registerUser() {
        validateRegister()

        binding.registerBtn.setOnClickListener { view ->
            val photoProfile = binding.photoProfileIv.drawable
            val name = binding.nameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            val user = RegisterRequest(
                fullName = name,
                email = email,
                password = password
            )

            when {
                photoProfile == null -> {
                    resources.getString(R.string.silahkan_upload_foto_profile).showSnackbar(
                        view = view,
                        context = this,
                        textColor = R.color.white,
                        backgroundColor = R.color.alert_danger
                    )
                }
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

    private fun imagePicker() {
        binding.profileImageContainer.setOnClickListener {
            TedImagePicker.with(this@RegisterActivity)
                .start { uri ->
                    Glide.with(this@RegisterActivity)
                        .load(uri)
                        .override(300)
                        .centerCrop()
                        .into(binding.photoProfileIv)
                }
            window.decorView.clearFocus()
        }
    }

    private fun dropDownMenu() {
        val city = resources.getStringArray(R.array.city)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_city_item, city)
        binding.autoCompleteCityTv.setAdapter(arrayAdapter)
    }

    private fun navigateToLogin() {
        binding.toLoginBtn.setOnClickListener {
            onBackPressed()
        }
    }
}