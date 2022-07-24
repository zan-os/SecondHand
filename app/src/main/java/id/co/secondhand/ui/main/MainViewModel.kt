package id.co.secondhand.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.notification.getnotification.GetNotificationUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    preferences: UserPreferences
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        splashScreen()
    }

    val token = preferences.getAccessToken().asLiveData()

    fun getNotification(accessToken: String) = getNotificationUseCase(accessToken)

    private fun splashScreen() {
        viewModelScope.launch {
            delay(1)
            _isLoading.value = false
        }
    }
}