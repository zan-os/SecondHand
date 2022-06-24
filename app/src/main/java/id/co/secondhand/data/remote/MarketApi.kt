package id.co.secondhand.data.remote

import id.co.secondhand.data.remote.request.LoginRequest
import id.co.secondhand.data.remote.request.RegisterRequest
import id.co.secondhand.data.remote.response.ProductDto
import id.co.secondhand.data.remote.response.ProductItemDto
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.RegisterDto
import id.co.secondhand.data.remote.response.auth.UserDataDto
import id.co.secondhand.data.remote.response.seller.AddProductDto
import id.co.secondhand.data.remote.response.seller.OrderDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MarketApi {

    @POST("auth/login")
    suspend fun authLogin(
        @Body user: LoginRequest
    ): LoginDto

    @POST("auth/register")
    suspend fun authRegister(
        @Body user: RegisterRequest
    ): RegisterDto

    @GET("auth/user")
    suspend fun getUserData(
        @Header("access_token") accessToken: String
    ): UserDataDto

    @GET("buyer/product")
    suspend fun getProducts(): ProductDto

    @GET("buyer/product/{id}")
    suspend fun getProductDetail(
        @Path("id") productId: Int
    ): ProductItemDto

    @Multipart
    @POST("seller/product")
    suspend fun addProduct(
        @Header("access_token") accessToken: String,
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("base_price") basePrice: RequestBody,
        @Part("category_ids") categoryIds: RequestBody,
        @Part("location") location: RequestBody,
    ): AddProductDto

    @GET("seller/product")
    suspend fun getSaleProduct(
        @Header("access_token") accessToken: String
    ): ProductDto

    @GET("seller/order")
    suspend fun getOrder(
        @Header("access_token") accessToken: String
    ): OrderDto
}