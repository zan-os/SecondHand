package id.co.secondhand.data.remote.response


import com.google.gson.annotations.SerializedName
import id.co.secondhand.data.local.entity.UserEntity
import id.co.secondhand.domain.model.Register

data class RegisterDto(
    @SerializedName("address")
    val address: String,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: Long,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("message")
    val message: String?
)

fun RegisterDto.toDomain(): Register =
    Register(
        address,
        createdAt,
        email,
        fullName,
        id,
        imageUrl,
        password,
        phoneNumber,
        updatedAt,
        name,
        message
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
