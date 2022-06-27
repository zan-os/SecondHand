package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.UserDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AuthRepository {

    suspend fun authLogin(user: LoginRequest): LoginDto

    suspend fun authRegister(
        fullName: RequestBody,
        email: RequestBody,
        password: RequestBody,
        phoneNumber: RequestBody,
        address: RequestBody,
        imageUrl: MultipartBody.Part,
        city: RequestBody
    ): UserDto

    suspend fun getUserData(accessToken: String): UserDto

    suspend fun editUserData(accessToken: String): UserDto
}