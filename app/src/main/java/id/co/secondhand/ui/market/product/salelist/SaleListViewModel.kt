package id.co.secondhand.ui.market.product.salelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.auth.getuser.GetUserUseCase
import id.co.secondhand.domain.usecase.market.seller.getorder.GetOrderUseCase
import id.co.secondhand.domain.usecase.market.seller.getsaleproduct.GetSaleProductUseCase
import javax.inject.Inject

@HiltViewModel
class SaleListViewModel @Inject constructor(
    private val getSaleListUseCase: GetSaleProductUseCase,
    private val getOrderUseCase: GetOrderUseCase,
    private val getUserUseCase: GetUserUseCase,
    preferences: UserPreferences
) : ViewModel() {

    val token = preferences.getAccessToken().asLiveData()

    fun getUserData(accessToken: String) = getUserUseCase(accessToken)

    fun getSaleProduct(accessToken: String) = getSaleListUseCase(accessToken)

    fun getOrder(accessToken: String) = getOrderUseCase(accessToken)
}