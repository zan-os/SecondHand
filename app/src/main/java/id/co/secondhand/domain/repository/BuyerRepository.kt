package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.response.buyer.DetailProductDto
import id.co.secondhand.data.remote.response.buyer.ProductItemDto

interface BuyerRepository {

    suspend fun getProducts(): List<ProductItemDto>

    suspend fun getProductDetail(productId: Int): DetailProductDto
}