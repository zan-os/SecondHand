package id.co.secondhand.ui.market.profile.update

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import id.co.secondhand.R
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityEditProfileBinding
import id.co.secondhand.domain.model.auth.User
import id.co.secondhand.utils.Extension.EXTRA_USER
import id.co.secondhand.utils.Extension.showSnackbar
import id.co.secondhand.utils.Extension.uriToFile
import java.io.File

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAccessToken()
        getUserData()
        arrowBack()
    }

    private fun getAccessToken() {
        viewModel.token.observe(this) { token ->
            saveUserData(token)
        }
    }

    fun getUserData() {
        val data = intent.getParcelableExtra<User>(EXTRA_USER) as User
        showUserData(data)
    }

    private fun showUserData(user: User) {
        binding.apply {
            Glide.with(this@EditProfileActivity)
                .load(user.imageUrl)
                .override(300)
                .centerCrop()
                .into(photoProfileIv)
            nameEt.setText(user.fullName)
            phoneNumberEt.setText(user.phoneNumber)
            autoCompleteCityTv.setText(user.city, false)
            addressEt.setText(user.address)
        }
    }

    private fun saveUserData(accessToken: String) {
        validateEdit()
        binding.apply {
            saveBtn.setOnClickListener {
                val fullName = nameEt.text.toString()
                val phoneNumber = phoneNumberEt.text.toString()
                val address = addressEt.text.toString()
                val city = autoCompleteCityTv.text.toString()

                viewModel.editUserData(
                    accessToken = accessToken,
                    imageUrl = getFile,
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    address = address,
                    city = city
                ).observe(this@EditProfileActivity) { result ->
                    when (result) {
                        is Resource.Loading -> {
                            Log.d("Market", "Loading")
                            showLoading(true)
                        }
                        is Resource.Success -> {
                            showLoading(false)
                            Log.d("Market", result.data.toString())
                            "Data berhasil disimpan".showSnackbar(
                                binding.root,
                                this@EditProfileActivity,
                                R.color.white,
                                R.color.alert_success
                            )
                        }
                        is Resource.Error -> {
                            showLoading(false)
                            showErrorMessage(result.message, it)
                            Log.d("Market", "Error ${result.message.toString()}")
                        }
                    }
                }
                window.decorView.clearFocus()
            }
        }
    }

    private fun validateEdit() {
        imagePicker()
        dropDownMenu()
        binding.addressEt.setRawInputType(InputType.TYPE_CLASS_TEXT)
    }

    private fun imagePicker() {
        binding.profileImageContainer.setOnClickListener {
            TedImagePicker.with(this)
                .start { uri ->
                    val file = uriToFile(uri, this@EditProfileActivity)
                    getFile = file
                    Glide.with(this)
                        .load(uri)
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
            binding.progressCircular.visibility =
                View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun showErrorMessage(code: String?, view: View) {
        when (code) {
            "400" -> {
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
        }
    }

    private fun arrowBack() {
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}