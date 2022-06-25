package id.co.secondhand.ui.market.profile.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.local.datastore.UserPreferences
import id.co.secondhand.domain.usecase.auth.getuser.GetUserUseCase
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    preferences: UserPreferences
) : ViewModel() {

    val token = preferences.getAccessToken().asLiveData()

    fun getUserData(accessToken: String) = getUserUseCase(accessToken)
}