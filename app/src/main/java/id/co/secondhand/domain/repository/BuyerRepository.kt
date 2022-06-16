package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.response.buyer.DetailProductDto
import id.co.secondhand.data.remote.response.buyer.ProductItemDto

interface BuyerRepository {

    suspend fun getProducts(token: String): List<ProductItemDto>

    suspend fun getProductDetail(token: String, productId: Int): DetailProductDto
}