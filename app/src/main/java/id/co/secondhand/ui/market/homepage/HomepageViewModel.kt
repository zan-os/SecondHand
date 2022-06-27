package id.co.secondhand.ui.market.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.market.buyer.getproducts.GetProductsUseCase
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    preferences: UserPreferences
) : ViewModel() {

    init {
        getProducts()
    }

    val token = preferences.getAccessToken().asLiveData()

    fun getProducts() = getProductsUseCase()
}