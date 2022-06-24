package id.co.secondhand.ui.market.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.market.seller.getsaleproduct.GetSaleProductUseCase
import javax.inject.Inject

@HiltViewModel
class SaleListViewModel @Inject constructor(
    private val useCase: GetSaleProductUseCase,
    preferences: UserPreferences
) : ViewModel() {

    val token = preferences.getAccessToken().asLiveData()

    fun getSaleProduct(accessToken: String) = useCase(accessToken)
}