package id.co.secondhand.data.repository

import id.co.secondhand.data.local.entity.UserEntity
import id.co.secondhand.data.local.room.dao.UserDao
import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.remote.request.LoginRequest
import id.co.secondhand.data.remote.request.RegisterRequest
import id.co.secondhand.data.remote.response.LoginDto
import id.co.secondhand.data.remote.response.RegisterDto
import id.co.secondhand.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val api: MarketApi
) : AuthRepository {

    override suspend fun authLogin(user: LoginRequest): LoginDto {
        return api.authLogin(user)
    }

    override suspend fun authRegister(user: RegisterRequest): RegisterDto {
        return api.authRegister(user)
    }

    override suspend fun saveUserData(user: UserEntity): Long {
        return dao.saveUserData(user)
    }
}