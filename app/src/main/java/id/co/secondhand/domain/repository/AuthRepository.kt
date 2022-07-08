package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.request.auth.RegisterRequest
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.UserDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AuthRepository {

    suspend fun authLogin(loginRequest: LoginRequest): LoginDto

    suspend fun authRegister(registerRequest: RegisterRequest): UserDto

    suspend fun getUserData(accessToken: String): UserDto

    suspend fun editUserData(
        accessToken: String,
        imageUrl: MultipartBody.Part?,
        fullName: RequestBody,
        phoneNumber: RequestBody,
        address: RequestBody,
        city: RequestBody
    ): UserDto
}