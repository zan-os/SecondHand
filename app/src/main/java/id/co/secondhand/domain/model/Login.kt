package id.co.secondhand.domain.model

data class Login(
    val accessToken: String?,
    val email: String?,
    val name: String?
)