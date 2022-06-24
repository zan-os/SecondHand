package id.co.secondhand.domain.repository

import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.request.auth.RegisterRequest
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.RegisterDto
import id.co.secondhand.data.remote.response.auth.UserDataDto
import id.co.secondhand.domain.model.auth.Register

interface AuthRepository {

    suspend fun authLogin(user: LoginRequest): LoginDto

    suspend fun authRegister(user: RegisterRequest): RegisterDto

    suspend fun getUserData(accessToken: String): UserDataDto
}