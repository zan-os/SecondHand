package id.co.secondhand.data.repository

import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.UserDto
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
        imageUrl: MultipartBody.Part,
        fullName: RequestBody,
        email: RequestBody,
        password: RequestBody,
        phoneNumber: RequestBody,
        address: RequestBody,
        city: RequestBody
    ): UserDto =
        api.authRegister(
            imageUrl,
            fullName,
            email,
            password,
            phoneNumber,
            address,
            city
        )

    override suspend fun getUserData(accessToken: String): UserDto =
        api.getUserData(accessToken)

    override suspend fun editUserData(
        accessToken: String,
        imageUrl: MultipartBody.Part?,
        fullName: RequestBody,
        phoneNumber: RequestBody,
        address: RequestBody,
        city: RequestBody
    ): UserDto =
        api.editUserData(
            accessToken,
            imageUrl,
            fullName,
            phoneNumber,
            address,
            city
        )
}