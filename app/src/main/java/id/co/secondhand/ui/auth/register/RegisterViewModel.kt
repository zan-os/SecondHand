package id.co.secondhand.ui.auth.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.secondhand.data.remote.request.auth.RegisterRequest
import id.co.secondhand.domain.usecase.auth.register.RegisterUseCase
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    ViewModel() {

    fun register(registerRequest: RegisterRequest) = registerUseCase(registerRequest)
}