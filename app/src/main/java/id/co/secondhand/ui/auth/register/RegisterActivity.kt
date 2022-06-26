package id.co.secondhand.ui.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityRegisterBinding
import id.co.secondhand.utils.Extension.dismissKeyboard
import id.co.secondhand.utils.Extension.showSnackbar
import id.co.secondhand.utils.Extension.uriToFile
import id.co.secondhand.utils.Extension.validateEmail
import id.co.secondhand.utils.Extension.validatePassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private var getImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerUser()
        navigateToLogin()
    }

    private fun registerUser() {
        validateRegister()

        binding.apply {
            registerBtn.setOnClickListener {
                window.decorView.clearFocus()
                it.dismissKeyboard()
                val name = nameEt.text.toString()
                val email = emailEt.text.toString()
                val password = passwordEt.text.toString()
                val phoneNumber = phoneNumberEt.text.toString()
                val city = autoCompleteCityTv.text.toString()
                val address = addressEt.text.toString()

                when {
                    getImage == null -> {
                        resources.getString(R.string.silahkan_upload_foto_profile).showSnackbar(
                            binding.root,
                            this@RegisterActivity,
                            R.color.white,
                            R.color.alert_danger
                        )
                    }
                    !email.validateEmail() -> {
                        binding.emailEtLayout.error =
                            resources.getString(R.string.email_tidak_valid)
                    }
                    !password.validatePassword() -> {
                        binding.passwordEtLayout.error =
                            resources.getString(R.string.min_6_karakter)
                    }
                    else -> {
                        viewModel.register(
                            imageUrl = getImage as File,
                            fullName = name,
                            email = email,
                            password = password,
                            phoneNumber = phoneNumber,
                            address = address,
                            city = city
                        ).observe(this@RegisterActivity) { result ->
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
                                        this@RegisterActivity,
                                        R.color.white,
                                        R.color.alert_success
                                    )
                                    lifecycleScope.launch {
                                        delay(1000)
                                        onBackPressed()
                                    }
                                }
                                is Resource.Error -> {
                                    showLoading(false)
                                    Log.d("Market", "Error ${result.message}")
                                    showErrorMessage(code = result.message, view = it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validateRegister() {
        imagePicker()
        dropDownMenu()

        binding.apply {
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val name = nameEt.text.toString()
                    val email = emailEt.text.toString()
                    val password = passwordEt.text.toString()
                    val numberPhone = phoneNumberEt.text.toString()
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
            phoneNumberEt.addTextChangedListener(textWatcher)
            autoCompleteCityTv.addTextChangedListener(textWatcher)
            addressEt.addTextChangedListener(textWatcher)
            addressEt.setRawInputType(InputType.TYPE_CLASS_TEXT)
        }
    }

    private fun imagePicker() {
        binding.profileImageContainer.setOnClickListener {
            TedImagePicker.with(this)
                .start { uri ->
                    val file = uriToFile(uri, this)
                    getImage = file
                    Glide.with(this)
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
                binding.emailEtLayout.error = "Email sudah terdaftar"
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