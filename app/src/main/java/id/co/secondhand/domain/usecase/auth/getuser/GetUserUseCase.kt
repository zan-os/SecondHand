package id.co.secondhand.domain.usecase.auth.getuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.co.secondhand.data.remote.response.auth.UserDataDto
import id.co.secondhand.data.resource.Resource
import id.co.secondhand.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(accessToken: String): LiveData<Resource<UserDataDto>> = liveData {
        try {
            emit(Resource.Loading())
            val data = repository.getUserData(accessToken)
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.code().toString()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connectivity"))
        }
    }
}