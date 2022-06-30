package id.co.secondhand.domain.usecase.notification.getnotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.notification.toDomain
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.model.notification.Notification
import id.co.secondhand.domain.repository.NotificationRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(accessToken: String): LiveData<Resource<List<Notification>>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.getNotification(accessToken).map { it.toDomain() }
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An Unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }

    }
}