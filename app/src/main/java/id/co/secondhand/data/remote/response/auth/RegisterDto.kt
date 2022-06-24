package id.co.secondhand.data.remote.response.auth


import com.google.gson.annotations.SerializedName
import id.co.secondhand.data.local.entity.UserEntity
import id.co.secondhand.domain.model.Register
import java.io.File

data class RegisterDto(
    @SerializedName("id")
    val id: Int,
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
    val city: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

fun RegisterDto.toDomain(): Register =
    Register(
        id,
        fullName,
        email,
        password,
        phoneNumber,
        address,
        imageUrl,
        city,
        createdAt,
        updatedAt
    )

fun RegisterDto.toUserEntity(): UserEntity =
    UserEntity(
        id,
        email,
        password,
        fullName,
        address,
        imageUrl,
        phoneNumber,
        createdAt,
        updatedAt,
    )
