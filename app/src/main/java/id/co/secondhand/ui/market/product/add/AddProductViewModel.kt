package id.co.secondhand.ui.market.product.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.data.remote.response.seller.AddProductDto
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.usecase.market.seller.addproduct.AddProductUseCase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val useCase: AddProductUseCase,
    preferences: UserPreferences
) : ViewModel() {

    val accessToken = preferences.getAccessToken().asLiveData()

    fun addProduct(
        accessToken: String,
        image: File,
        name: String,
        description: String,
        basePrice: String,
        categoryIds: String,
        location: String,
    ): LiveData<Resource<AddProductDto>> {
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
        val requestName = name.toRequestBody("text/plain".toMediaType())
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestBasePrice = basePrice.toRequestBody("text/plain".toMediaType())
        val requestCategoryIds = categoryIds.toRequestBody("text/plain".toMediaType())
        val requestLocation = location.toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            image.name,
            requestImageFile
        )

        return useCase(
            accessToken,
            imageMultipart,
            requestName,
            requestDescription,
            requestBasePrice,
            requestCategoryIds,
            requestLocation
        )
    }
}