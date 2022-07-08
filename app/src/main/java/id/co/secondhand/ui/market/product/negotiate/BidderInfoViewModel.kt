package id.co.secondhand.ui.market.product.negotiate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.market.seller.BidderInfo.BidderInfoUseCase
import javax.inject.Inject

@HiltViewModel
class BidderInfoViewModel @Inject constructor(
    private val useCase: BidderInfoUseCase,
    preferences: UserPreferences
): ViewModel(){
    var accessToken = MutableLiveData<String>()

    val token = preferences.getAccessToken().asLiveData()

    fun BidderInfoActivity(accessToken: String, orderId: Int) =
        useCase(accessToken, orderId)
}