package id.co.secondhand.data.repository

import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.response.buyer.DetailProductDto
import id.co.secondhand.data.remote.response.buyer.ProductItemDto
import id.co.secondhand.domain.repository.BuyerRepository
import javax.inject.Inject

class BuyerRepositoryImpl @Inject constructor(
    private val api: MarketApi
) : BuyerRepository {
    override suspend fun getProducts(token: String): List<ProductItemDto> {
        return api.getProducts(token = token)
    }

    override suspend fun getProductDetail(token: String, productId: Int): DetailProductDto {
        return api.getProductDetail(token = token, productId = productId)
    }
}