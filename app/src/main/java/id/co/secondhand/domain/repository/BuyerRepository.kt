package id.co.secondhand.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import id.co.secondhand.data.remote.request.product.BargainRequest
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.data.remote.response.buyer.ProductDto
import id.co.secondhand.domain.model.buyer.Product

interface BuyerRepository {

    fun getProducts(query: String, categoryId: Int?): LiveData<PagingData<Product>>

    suspend fun getProductDetail(productId: Int): ProductDto

    suspend fun bargainProduct(accessToken: String, bargainRequest: BargainRequest): OrderDtoItem
}