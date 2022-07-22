package id.co.secondhand.domain.usecase.market.seller.getsaleproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.buyer.toDomain
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.model.buyer.Product
import id.co.secondhand.domain.repository.SellerRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSaleProductUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(accessToken: String): LiveData<Resource<List<Product>>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.getSaleProduct(accessToken).map { it.toDomain() }
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}