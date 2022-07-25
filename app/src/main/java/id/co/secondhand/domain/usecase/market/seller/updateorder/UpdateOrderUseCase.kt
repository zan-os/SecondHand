package id.co.secondhand.domain.usecase.market.seller.updateorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.repository.SellerRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(accessToken: String, id: Int, status: String): LiveData<Resource<OrderDtoItem>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.updateOrder(accessToken, id, status)
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}