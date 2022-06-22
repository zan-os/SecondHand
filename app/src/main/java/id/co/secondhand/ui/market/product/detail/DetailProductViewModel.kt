package id.co.secondhand.ui.market.product.detail

import android.content.ClipData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.market.buyer.getproductdetail.GetProductDetailUseCase
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val useCase: GetProductDetailUseCase,
    preferences: UserPreferences
) : ViewModel() {

    var accessToken = MutableLiveData<String>()

    val token = preferences.getAccessToken().asLiveData()

    fun getDetailProduct(productId: Int) =
        useCase(productId = productId)
}