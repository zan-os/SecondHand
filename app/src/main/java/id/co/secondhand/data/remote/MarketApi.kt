package id.co.secondhand.data.remote

import id.co.secondhand.data.remote.request.auth.LoginRequest
import id.co.secondhand.data.remote.request.auth.RegisterRequest
import id.co.secondhand.data.remote.request.product.BargainRequest
import id.co.secondhand.data.remote.response.OrderDto
import id.co.secondhand.data.remote.response.OrderDtoItem
import id.co.secondhand.data.remote.response.ProductItemDto
import id.co.secondhand.data.remote.response.auth.LoginDto
import id.co.secondhand.data.remote.response.auth.UserDto
import id.co.secondhand.data.remote.response.notification.NotificationDto
import id.co.secondhand.data.remote.response.seller.AddProductDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MarketApi {

    @POST("auth/login")
    suspend fun authLogin(
        @Body loginRequest: LoginRequest
    ): LoginDto

    @POST("auth/register")
    suspend fun authRegister(
        @Body registerRequest: RegisterRequest
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
    suspend fun getProducts(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("category_id") categoryId: Int?
    ): List<id.co.secondhand.data.remote.response.buyer.ProductDto>

    @GET("buyer/product/{id}")
    suspend fun getProductDetail(
        @Path("id") productId: Int
    ): id.co.secondhand.data.remote.response.buyer.ProductDto

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
    ): List<id.co.secondhand.data.remote.response.buyer.ProductDto>

    @GET("seller/order")
    suspend fun getOrder(
        @Header("access_token") accessToken: String,
        @Query("status") status: String
    ): OrderDto

    @GET("seller/order/{id}")
    suspend fun getOrderId(
        @Header("access_token") accessToken: String,
        @Path("id") OrderId: Int
    ): OrderDtoItem

    @FormUrlEncoded
    @PATCH("seller/order/{id}")
    suspend fun updateOrder(
        @Header("access_token") accessToken: String,
        @Path("id") OrderId: Int,
        @Field("status") status: String
    ): OrderDtoItem

    @GET("notification")
    suspend fun getNotification(
        @Header("access_token") accessToken: String
    ): List<NotificationDto>

    @PATCH("notification/{id}")
    suspend fun readNotification(
        @Header("access_token") accessToken: String,
        @Path("id") id: Int
    ): NotificationDto

    @POST("buyer/order")
    suspend fun bargainProduct(
        @Header("access_token") accessToken: String,
        @Body bargainRequest: BargainRequest
    ): OrderDtoItem
}