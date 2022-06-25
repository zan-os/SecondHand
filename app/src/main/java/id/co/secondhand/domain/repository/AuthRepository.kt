package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.RegisterDto
import id.co.secondhand.data.remote.response.auth.UserDataDto
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
    ): RegisterDto

    suspend fun getUserData(accessToken: String): UserDataDto
}