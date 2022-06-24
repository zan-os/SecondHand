package id.co.secondhand.domain.model.auth

data class Login(
    val name: String,
    val email: String,
    val accessToken: String
)