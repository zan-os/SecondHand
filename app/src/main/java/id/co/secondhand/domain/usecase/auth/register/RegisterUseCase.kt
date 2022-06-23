package id.co.secondhand.domain.usecase.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.request.RegisterRequest
import id.co.secondhand.data.remote.response.toDomain
import id.co.secondhand.data.remote.response.toUserEntity
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.model.Register
import id.co.secondhand.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke(user: RegisterRequest): LiveData<Resource<Register>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.authRegister(user)
            repository.saveUserData(data.toUserEntity())
            emit(Resource.Success(data.toDomain()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}