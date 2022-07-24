package id.co.secondhand.data.remote.response


import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("address")
    val address: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?
)