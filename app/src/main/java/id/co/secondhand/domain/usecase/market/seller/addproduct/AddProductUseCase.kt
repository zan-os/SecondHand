package id.co.secondhand.domain.usecase.market.seller.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.seller.AddProductDto
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.repository.SellerRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val repository: SellerRepository
) {

    operator fun invoke(
        accessToken: String,
        image: MultipartBody.Part,
        name: RequestBody,
        description: RequestBody,
        basePrice: RequestBody,
        categoryIds: RequestBody,
        location: RequestBody,
    ): LiveData<Resource<AddProductDto>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.addProduct(
                accessToken,
                image,
                name,
                description,
                basePrice,
                categoryIds,
                location
            )
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}