package id.co.secondhand.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.remote.response.auth.UserDto
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.usecase.auth.register.RegisterUseCase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    ViewModel() {

    fun register(
        imageUrl: File,
        fullName: String,
        email: String,
        password: String,
        phoneNumber: String,
        address: String,
        city: String
    ): LiveData<Resource<UserDto>> {
        val requestImageFile = imageUrl.asRequestBody("image/jpeg".toMediaType())
        val requestFullName = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestEmail = email.toRequestBody("text/plain".toMediaType())
        val requestPassword = password.toRequestBody("text/plain".toMediaType())
        val requestPhoneNumber = phoneNumber.toRequestBody("text/plain".toMediaType())
        val requestAddress = address.toRequestBody("text/plain".toMediaType())
        val requestCity = city.toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            imageUrl.name,
            requestImageFile
        )

        return registerUseCase(
            imageMultipart,
            requestFullName,
            requestEmail,
            requestPassword,
            requestPhoneNumber,
            requestAddress,
            requestCity
        )
    }
}