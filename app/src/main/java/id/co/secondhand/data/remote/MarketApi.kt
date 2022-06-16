package id.co.secondhand.data.remote

import id.co.secondhand.data.remote.request.LoginRequest
import id.co.secondhand.data.remote.request.RegisterRequest
import id.co.secondhand.data.remote.response.LoginDto
import id.co.secondhand.data.remote.response.ProductDto
import id.co.secondhand.data.remote.response.RegisterDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MarketApi {

    @POST("auth/login")
    suspend fun authLogin(
        @Body user: LoginRequest
    ): LoginDto

    @POST("auth/register")
    suspend fun authRegister(
        @Body user: RegisterRequest
    ): RegisterDto

    @GET("buyer/product")
    suspend fun getProducts(
        @Header("access_token") token: String
    ): ProductDto
}