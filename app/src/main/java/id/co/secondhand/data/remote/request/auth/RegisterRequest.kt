package id.co.secondhand.data.remote.request.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("full_name")
    val fullName: String,
    val email: String,
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: Long,
    val address: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val city: String
)