package id.co.secondhand.ui.market.notification.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.market.seller.getorder.GetOrderUseCase
import id.co.secondhand.domain.usecase.notification.getnotification.GetNotificationUseCase
import javax.inject.Inject

@HiltViewModel
class SellerNotificationViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val getOrderUseCase: GetOrderUseCase,
    preferences: UserPreferences
) : ViewModel() {

    val token = preferences.getAccessToken().asLiveData()

    fun getNotification(accessToken: String) = getNotificationUseCase(accessToken)

    fun getOrder(accessToken: String) = getOrderUseCase(accessToken)
}