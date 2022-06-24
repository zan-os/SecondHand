package id.co.secondhand.data.remote.response.auth


import com.google.gson.annotations.SerializedName
import id.co.secondhand.domain.model.auth.Register

data class RegisterDto(
    val id: Int,
    @SerializedName("full_name")
    val fullName: String,
    val email: String,
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: Long,
    val address: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val city: String,
    val createdAt: String,
    val updatedAt: String
)

fun RegisterDto.toDomain(): Register =
    Register(
        fullName,
        email,
        password,
        phoneNumber,
        address,
        imageUrl,
        city
    )