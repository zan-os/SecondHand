package id.co.secondhand.data.remote

import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.response.ProductDto
import id.co.secondhand.data.remote.response.ProductItemDto
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.UserDto
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

    @Multipart
    @POST("auth/register")
    suspend fun authRegister(
        @Part imageUrl: MultipartBody.Part,
        @Part("full_name") fullName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("address") address: RequestBody,
        @Part("city") city: RequestBody
    ): UserDto

    @GET("auth/user")
    suspend fun getUserData(
        @Header("access_token") accessToken: String
    ): UserDto

    @Multipart
    @PUT("auth/user")
    suspend fun editUserData(
        @Header("access_token") accessToken: String,
        @Part imageUrl: MultipartBody.Part?,
        @Part("full_name") fullName: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("address") address: RequestBody,
        @Part("city") city: RequestBody
    ): UserDto

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