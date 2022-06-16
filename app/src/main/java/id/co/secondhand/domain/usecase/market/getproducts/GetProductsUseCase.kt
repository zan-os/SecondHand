package id.co.secondhand.domain.usecase.market.getproducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.toDomain
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.model.Product
import id.co.secondhand.domain.repository.MarketRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: MarketRepository
) {
    operator fun invoke(token: String): LiveData<Resource<List<Product>>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.getProducts(token).map { it.toDomain() }
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}