package id.co.secondhand.data.repository

import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.response.notification.NotificationDtoItem
import id.co.secondhand.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: MarketApi
) : NotificationRepository {

    override suspend fun getNotification(accessToken: String): List<NotificationDtoItem> =
        api.getNotification(accessToken)
}