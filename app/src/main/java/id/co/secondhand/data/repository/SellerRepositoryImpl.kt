package id.co.secondhand.data.repository

import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.response.seller.AddProductDto
import id.co.secondhand.domain.repository.SellerRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class SellerRepositoryImpl @Inject constructor(
    private val api: MarketApi
) : SellerRepository {
    override suspend fun addProduct(
        accessToken: String,
        image: MultipartBody.Part,
        name: RequestBody,
        description: RequestBody,
        basePrice: RequestBody,
        categoryIds: RequestBody,
        location: RequestBody,
    ): AddProductDto {
        return api.addProduct(
            accessToken,
            image,
            name,
            description,
            basePrice,
            categoryIds,
            location
        )
    }
}