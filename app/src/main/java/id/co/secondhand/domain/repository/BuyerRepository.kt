package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.request.product.BargainRequest
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.data.remote.response.buyer.ProductDto

interface BuyerRepository {

    suspend fun getProducts(): List<ProductDto>

    suspend fun getProductDetail(productId: Int): ProductDto

    suspend fun bargainProduct(accessToken: String, bargainRequest: BargainRequest): OrderDtoItem
}