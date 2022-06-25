package id.co.secondhand.data.remote

import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.RegisterDto
import id.co.secondhand.data.remote.response.auth.UserDataDto
import id.co.secondhand.data.remote.response.buyer.DetailProductDto
import id.co.secondhand.data.remote.response.buyer.ProductDto
import id.co.secondhand.data.remote.response.seller.AddProductDto
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
        @Part("full_name") fullName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("address") address: RequestBody,
        @Part imageUrl: MultipartBody.Part,
        @Part("city") city: RequestBody
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
    ): DetailProductDto

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
}