package id.co.secondhand.ui.market.profile.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.data.remote.response.auth.UserDto
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.usecase.auth.edituser.EditUserUseCase
import id.co.secondhand.domain.usecase.auth.getuser.GetUserUseCase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val editUserUseCase: EditUserUseCase,
    preferences: UserPreferences
) : ViewModel() {

    val token = preferences.getAccessToken().asLiveData()

    fun getUserData(accessToken: String) = getUserUseCase(accessToken)

    fun editUserData(
        accessToken: String,
        imageUrl: File?,
        fullName: String,
        phoneNumber: String,
        address: String,
        city: String
    ): LiveData<Resource<UserDto>> {
        val requestImageFile = imageUrl?.asRequestBody("image/jpeg".toMediaType())
        val requestFullName = fullName.toRequestBody("text/plain".toMediaType())
        val requestPhoneNumber = phoneNumber.toRequestBody("text/plain".toMediaType())
        val requestAddress = address.toRequestBody("text/plain".toMediaType())
        val requestCity = city.toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part? = requestImageFile?.let {
            MultipartBody.Part.createFormData(
                "image",
                imageUrl.name,
                it
            )
        }

        return editUserUseCase(
            accessToken,
            imageMultipart,
            requestFullName,
            requestPhoneNumber,
            requestAddress,
            requestCity
        )
    }
}