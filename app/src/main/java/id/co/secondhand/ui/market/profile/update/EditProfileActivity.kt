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
import id.co.secondhand.data.remote.response.auth.UserDataDto
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.databinding.ActivityEditProfileBinding

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAccessToken()
        saveUserData()
        arrowBack()
    }

    private fun getAccessToken() {
        viewModel.token.observe(this) { token ->
            getUserData(token)
        }
    }

    private fun getUserData(token: String) {
        viewModel.getUserData(token).observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("Market", "Loading")
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    Log.d("Market", result.data.toString())
                    result.data?.let { showUserData(it) }
                }
                is Resource.Error -> {
                    showLoading(false)
                    Log.d("Market", "Error ${result.message.toString()}")
                }
            }
        }
    }

    private fun showUserData(user: UserDataDto) {
        binding.apply {
            Glide.with(this@EditProfileActivity)
                .load(user.imageUrl)
                .override(300)
                .centerCrop()
                .into(photoProfileIv)
            nameEt.setText(user.fullName)
            phoneNumberEt.setText(user.phoneNumber)
            autoCompleteCityTv.setText(user.city)
            addressEt.setText(user.address)
        }
    }

    private fun showLoading(value: Boolean) {
        if (value) {
            binding.progressCircular.visibility =
                View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun saveUserData() {
        validateEdit()
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

    private fun arrowBack() {
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}