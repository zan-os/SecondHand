package id.co.secondhand.data.repository

import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.response.ProductItemDto
import id.co.secondhand.domain.repository.MarketRepository
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val api: MarketApi
) : MarketRepository {
    override suspend fun getProducts(token: String): List<ProductItemDto> {
        return api.getProducts(token)
    }
}