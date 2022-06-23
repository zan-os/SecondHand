package id.co.secondhand.domain.usecase.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.request.LoginRequest
import id.co.secondhand.data.remote.response.toDomain
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.model.Login
import id.co.secondhand.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke(user: LoginRequest): LiveData<Resource<Login>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.authLogin(user).toDomain()
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}