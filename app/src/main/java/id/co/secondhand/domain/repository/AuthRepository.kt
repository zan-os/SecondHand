package id.co.secondhand.domain.repository

import id.co.secondhand.data.local.entity.UserEntity
import id.co.secondhand.data.remote.request.LoginRequest
import id.co.secondhand.data.remote.request.RegisterRequest
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.RegisterDto
import id.co.secondhand.data.remote.response.auth.UserDataDto

interface AuthRepository {

    suspend fun authLogin(user: LoginRequest): LoginDto

    suspend fun authRegister(user: RegisterRequest): RegisterDto

    suspend fun getUserData(accessToken: String): UserDataDto
}