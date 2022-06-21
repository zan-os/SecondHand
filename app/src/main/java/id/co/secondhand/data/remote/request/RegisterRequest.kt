package id.co.secondhand.data.remote.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: Long? = 0,
    @SerializedName("address")
    val address: String? = "null",
    @SerializedName("image_url")
    val imageUrl: String? = "null",
    @SerializedName("city")
    val city: String? = "null"
)
