package id.co.secondhand.domain.usecase.market.buyer.bargainproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.request.product.BargainRequest
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.repository.BuyerRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class BargainProductUseCase @Inject constructor(private val repository: BuyerRepository) {
    operator fun invoke(
        accessToken: String,
        bargainRequest: BargainRequest
    ): LiveData<Resource<OrderDtoItem>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.bargainProduct(accessToken, bargainRequest)
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("No Internet Connection"))
        }
    }
}