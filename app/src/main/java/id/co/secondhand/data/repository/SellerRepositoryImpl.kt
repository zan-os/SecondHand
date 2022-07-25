package id.co.secondhand.data.repository

import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.data.remote.response.buyer.ProductDto
import id.co.secondhand.data.remote.response.seller.AddProductDto
import id.co.secondhand.domain.repository.SellerRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    override suspend fun getSaleProduct(accessToken: String): List<ProductDto> {
        return api.getSaleProduct(accessToken)
    }

    override suspend fun getOrder(accessToken: String, status: String): List<OrderDtoItem> {
        return api.getOrder(accessToken, status)
    }

    override suspend fun getOrderSellerId(accessToken: String, id: Int): OrderDtoItem {
        return api.getOrderId(accessToken, id)
    }

    override suspend fun updateOrder(accessToken: String, id: Int, status: String): OrderDtoItem {
        return api.updateOrder(accessToken, id, status)
    }
}