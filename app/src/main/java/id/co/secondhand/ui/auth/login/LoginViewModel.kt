package id.co.secondhand.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.domain.usecase.auth.login.LoginUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: LoginUseCase,
    private val preferences: UserPreferences
) : ViewModel() {

    fun login(user: LoginRequest) = useCase(user)

    val token = preferences.getAccessToken().asLiveData()

    fun saveAccessToken(token: String) {
        viewModelScope.launch {
            preferences.accessToken(token)
        }
    }
}