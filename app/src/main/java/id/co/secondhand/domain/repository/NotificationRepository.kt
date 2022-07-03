package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.response.notification.NotificationDtoItem

interface NotificationRepository {

    suspend fun getNotification(accessToken: String): List<NotificationDtoItem>
}