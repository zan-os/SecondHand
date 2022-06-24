package id.co.secondhand.data.remote.response.auth


import com.google.gson.annotations.SerializedName
import id.co.secondhand.domain.model.Login

data class LoginDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String
)

fun LoginDto.toDomain(): Login =
    Login(accessToken, email, name)