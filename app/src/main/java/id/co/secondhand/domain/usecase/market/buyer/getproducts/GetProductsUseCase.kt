package id.co.secondhand.domain.usecase.market.buyer.getproducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.toDomain
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.model.Product
import id.co.secondhand.domain.repository.BuyerRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: BuyerRepository
) {
    operator fun invoke(): LiveData<Resource<List<Product>>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.getProducts().map { it.toDomain() }
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}