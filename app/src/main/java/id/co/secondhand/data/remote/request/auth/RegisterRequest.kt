package id.co.secondhand.data.remote.request.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: Long,
    @SerializedName("address")
    val address: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("city")
    val city: String
)
