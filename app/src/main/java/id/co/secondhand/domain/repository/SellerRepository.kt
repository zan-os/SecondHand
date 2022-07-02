package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.response.ProductItemDto
import id.co.secondhand.data.remote.response.seller.AddProductDto
import id.co.secondhand.data.remote.response.seller.OrderDtoItem
import id.co.secondhand.data.remote.response.seller.OrderSellerDtoItem
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SellerRepository {

    suspend fun addProduct(
        accessToken: String,
        image: MultipartBody.Part,
        name: RequestBody,
        description: RequestBody,
        basePrice: RequestBody,
        categoryIds: RequestBody,
        location: RequestBody,
    ): AddProductDto

    suspend fun getSaleProduct(accessToken: String): List<ProductItemDto>

    suspend fun getOrder(accessToken: String): List<OrderDtoItem>

    suspend fun getOrderSeller(accessToken: String): List<OrderSellerDtoItem>

    suspend fun getOrderSellerId(accessToken: String, id: Int): OrderSellerDtoItem
}