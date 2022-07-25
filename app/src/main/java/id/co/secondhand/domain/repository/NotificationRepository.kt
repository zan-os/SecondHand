package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.response.notification.NotificationDto

interface NotificationRepository {

    suspend fun getNotification(accessToken: String): List<NotificationDto>

    suspend fun readNotification(accessToken: String, id: Int): NotificationDto
}