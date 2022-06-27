package id.co.secondhand.domain.usecase.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.auth.UserDto
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.repository.AuthRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke(
        fullName: RequestBody,
        email: RequestBody,
        password: RequestBody,
        phoneNumber: RequestBody,
        address: RequestBody,
        imageUrl: MultipartBody.Part,
        city: RequestBody
    ): LiveData<Resource<UserDto>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.authRegister(
                fullName,
                email,
                password,
                phoneNumber,
                address,
                imageUrl,
                city
            )
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}