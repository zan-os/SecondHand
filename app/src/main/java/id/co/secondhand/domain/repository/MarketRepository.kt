package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.response.ProductItemDto

interface MarketRepository {

    suspend fun getProducts(token: String): List<ProductItemDto>
}