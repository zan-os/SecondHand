package id.co.secondhand.data.repository

import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.request.product.BargainRequest
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.data.remote.response.buyer.ProductDto
import id.co.secondhand.domain.repository.BuyerRepository
import javax.inject.Inject

class BuyerRepositoryImpl @Inject constructor(
    private val api: MarketApi
) : BuyerRepository {
    override suspend fun getProducts(): List<ProductDto> {
        return api.getProducts()
    }

    override suspend fun getProductDetail(productId: Int): ProductDto {
        return api.getProductDetail(productId = productId)
    }

    override suspend fun bargainProduct(
        accessToken: String,
        bargainRequest: BargainRequest
    ): OrderDtoItem {
        return api.bargainProduct(accessToken, bargainRequest)
    }
}