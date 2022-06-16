package id.co.secondhand.ui.market.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel() {

    fun clearCredential() {
        viewModelScope.launch {
            preferences.clearToken()
        }
    }
}