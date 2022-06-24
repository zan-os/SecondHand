package id.co.secondhand.ui.market.profile.update

import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import gun0912.tedimagepicker.builder.TedImagePicker
import id.co.secondhand.R
import id.co.secondhand.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveUser()
        arrowBack()
    }

    private fun saveUser() {
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

    private fun arrowBack() {
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}