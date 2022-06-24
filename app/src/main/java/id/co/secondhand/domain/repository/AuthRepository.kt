package id.co.secondhand.domain.repository

import id.co.secondhand.data.local.entity.UserEntity
import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.request.auth.RegisterRequest
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.RegisterDto
import id.co.secondhand.data.remote.response.auth.UserDataDto

interface AuthRepository {

    suspend fun authLogin(user: LoginRequest): LoginDto

    suspend fun authRegister(user: RegisterRequest): RegisterDto

    suspend fun saveUserData(user: UserEntity): Long

    suspend fun getUserData(accessToken: String): UserDataDto
}