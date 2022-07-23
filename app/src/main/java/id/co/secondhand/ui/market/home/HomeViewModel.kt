package id.co.secondhand.ui.market.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.market.buyer.getproducts.GetProductsUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    preferences: UserPreferences,
    state: SavedStateHandle
) : ViewModel() {

    val token = preferences.getAccessToken().asLiveData()

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val product = currentQuery.switchMap { queryString ->
        getProductsUseCase(queryString, null).cachedIn(viewModelScope)
    }

    fun searchByCategory(categoryId: Int?) = currentQuery.switchMap { queryString ->
        getProductsUseCase(queryString, categoryId)
    }

    fun getProducts(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = ""
    }
}