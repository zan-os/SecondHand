package id.co.secondhand.data.remote.response.auth

import com.google.gson.annotations.SerializedName
import id.co.secondhand.domain.model.auth.User

data class UserDto(
    val id: Int,
    @SerializedName("full_name")
    val fullName: String,
    val email: String,
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val address: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val city: String,
    val createdAt: String,
    val updatedAt: String
)

fun UserDto.toDomain() =
    User(fullName, phoneNumber, address, imageUrl, city)
