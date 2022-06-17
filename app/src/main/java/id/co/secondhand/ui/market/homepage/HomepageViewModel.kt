package id.co.secondhand.ui.market.homepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.market.buyer.getproducts.GetProductsUseCase
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val useCase: GetProductsUseCase,
    preferences: UserPreferences
) : ViewModel() {

    var accessToken = MutableLiveData<String>()

    val token = preferences.getAccessToken().asLiveData()

    init {
        getProducts()
    }

    fun getProducts() = useCase()
}