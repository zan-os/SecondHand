package id.co.secondhand.domain.usecase.market.buyer.getproducts

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import id.co.secondhand.domain.model.buyer.Product
import id.co.secondhand.domain.repository.BuyerRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: BuyerRepository
) {
    operator fun invoke(query: String, categoryId: Int?): LiveData<PagingData<Product>> {
        return repository.getProducts(query, categoryId)
    }
}