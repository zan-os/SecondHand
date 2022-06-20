package id.co.secondhand.domain.repository

import id.co.secondhand.data.local.entity.UserEntity
import id.co.secondhand.data.remote.request.LoginRequest
import id.co.secondhand.data.remote.request.RegisterRequest
import id.co.secondhand.data.remote.response.LoginDto
import id.co.secondhand.data.remote.response.RegisterDto

interface AuthRepository {

    suspend fun authLogin(user: LoginRequest): LoginDto

    suspend fun authRegister(user: RegisterRequest): RegisterDto

    suspend fun saveUserData(user: UserEntity): Long
}