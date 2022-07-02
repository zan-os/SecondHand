package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.response.ProductItemDto

interface BuyerRepository {

    suspend fun getProducts(): List<ProductItemDto>

    suspend fun getProductDetail(productId: Int): ProductItemDto
}