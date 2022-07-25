package id.co.secondhand.ui.market.product.bidderinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.market.seller.productMatch.ProductMatchUseCase
import id.co.secondhand.domain.usecase.market.seller.updateorder.UpdateOrderUseCase
import javax.inject.Inject

@HiltViewModel
class BidderInfoViewModel @Inject constructor(
    private val productMatchUseCase: ProductMatchUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase,
    preferences: UserPreferences
) : ViewModel() {

    val token = preferences.getAccessToken().asLiveData()

    fun getOrderData(accessToken: String, orderId: Int) = productMatchUseCase(accessToken, orderId)

    fun updateOrder(accessToken: String, orderId: Int, status: String) =
        updateOrderUseCase(accessToken, orderId, status)
}