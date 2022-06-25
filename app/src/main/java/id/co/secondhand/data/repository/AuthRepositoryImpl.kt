package id.co.secondhand.data.repository

import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.RegisterDto
import id.co.secondhand.data.remote.response.auth.UserDataDto
import id.co.secondhand.domain.repository.AuthRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: MarketApi
) : AuthRepository {

    override suspend fun authLogin(user: LoginRequest): LoginDto =
        api.authLogin(user)

    override suspend fun authRegister(
        fullName: RequestBody,
        email: RequestBody,
        password: RequestBody,
        phoneNumber: RequestBody,
        address: RequestBody,
        imageUrl: MultipartBody.Part,
        city: RequestBody
    ): RegisterDto =
        api.authRegister(
            fullName,
            email,
            password,
            phoneNumber,
            address,
            imageUrl,
            city
        )

    override suspend fun getUserData(accessToken: String): UserDataDto =
        api.getUserData(accessToken)
}