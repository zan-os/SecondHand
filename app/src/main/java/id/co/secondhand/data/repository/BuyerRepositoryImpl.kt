package id.co.secondhand.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.MarketPagingSource
import id.co.secondhand.data.remote.request.product.BargainRequest
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.data.remote.response.buyer.ProductDto
import id.co.secondhand.domain.model.buyer.Product
import id.co.secondhand.domain.repository.BuyerRepository
import javax.inject.Inject

class BuyerRepositoryImpl @Inject constructor(
    private val api: MarketApi
) : BuyerRepository {
    override fun getProducts(query: String): LiveData<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                maxSize = 60,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MarketPagingSource(api, query)
            }
        ).liveData
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