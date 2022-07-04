package id.co.secondhand.ui.market.notification.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.notification.getnotification.GetNotificationUseCase
import id.co.secondhand.domain.usecase.notification.readnotification.ReadNotificationUseCase
import javax.inject.Inject

@HiltViewModel
class SellerNotificationViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
    preferences: UserPreferences
) : ViewModel() {

    val token = preferences.getAccessToken().asLiveData()

    fun getNotification(accessToken: String) = getNotificationUseCase(accessToken)

    fun readNotification(accessToken: String, id: Int) = readNotificationUseCase(accessToken, id)
}