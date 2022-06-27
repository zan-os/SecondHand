package id.co.secondhand.domain.usecase.market.seller.getorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.seller.OrderDtoItem
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.repository.SellerRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetOrderUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(accessToken: String): LiveData<Resource<List<OrderDtoItem>>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.getOrder(accessToken)
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}