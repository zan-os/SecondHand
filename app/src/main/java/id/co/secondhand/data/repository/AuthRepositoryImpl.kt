package id.co.secondhand.data.repository

import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.request.auth.RegisterRequest
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.RegisterDto
import id.co.secondhand.data.remote.response.auth.UserDataDto
import id.co.secondhand.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: MarketApi
) : AuthRepository {

    override suspend fun authLogin(user: LoginRequest): LoginDto {
        return api.authLogin(user)
    }

    override suspend fun authRegister(user: RegisterRequest): RegisterDto {
        return api.authRegister(user)
    }

    override suspend fun getUserData(accessToken: String): UserDataDto {
        return api.getUserData(accessToken)
    }
}