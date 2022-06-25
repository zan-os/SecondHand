package id.co.secondhand.data.remote.response.auth

import com.google.gson.annotations.SerializedName
import id.co.secondhand.domain.model.auth.Login

data class LoginDto(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("access_token")
    val accessToken: String
)

fun LoginDto.toDomain(): Login =
    Login(name, email, accessToken)