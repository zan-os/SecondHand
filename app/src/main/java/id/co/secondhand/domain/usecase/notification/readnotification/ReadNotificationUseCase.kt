package id.co.secondhand.domain.usecase.notification.readnotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.notification.toDomain
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.model.notification.Notification
import id.co.secondhand.domain.repository.NotificationRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ReadNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(accessToken: String, id: Int): LiveData<Resource<Notification>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.readNotification(accessToken, id).toDomain()
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An Unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}