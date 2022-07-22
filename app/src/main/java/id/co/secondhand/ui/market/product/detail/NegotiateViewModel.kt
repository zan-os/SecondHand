package id.co.secondhand.ui.market.product.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.data.remote.request.product.BargainRequest
import id.co.secondhand.domain.usecase.market.buyer.bargainproduct.BargainProductUseCase
import javax.inject.Inject

@HiltViewModel
class NegotiateViewModel @Inject constructor(
    private val bargainProductUseCase: BargainProductUseCase,
    preferences: UserPreferences
) : ViewModel() {

    val accessToken = preferences.getAccessToken().asLiveData()

    fun bargainProduct(accessToken: String, bargainRequest: BargainRequest) =
        bargainProductUseCase(accessToken, bargainRequest)
}