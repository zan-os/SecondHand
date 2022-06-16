package id.co.secondhand.domain.usecase.market.buyer.getproductdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.buyer.toDomain
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.model.buyer.DetailProduct
import id.co.secondhand.domain.repository.BuyerRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val repository: BuyerRepository
) {
    operator fun invoke(token: String, productId: Int): LiveData<Resource<DetailProduct>> =
        liveData {
            try {
                emit(Resource.Loading())
                val data =
                    repository.getProductDetail(token = token, productId = productId).toDomain()
                emit(Resource.Success(data))
            } catch (e: HttpException) {
                emit(Resource.Error(e.code().toString()))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
            }
        }
}