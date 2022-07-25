package id.co.secondhand.ui.market.product.productmatch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.market.seller.productMatch.ProductMatchUseCase
import javax.inject.Inject

@HiltViewModel
class ProductMatchViewModel @Inject constructor(
    private val useCase: ProductMatchUseCase,
    preferences: UserPreferences
) : ViewModel() {
    var accessToken = MutableLiveData<String>()

    val token = preferences.getAccessToken().asLiveData()


}