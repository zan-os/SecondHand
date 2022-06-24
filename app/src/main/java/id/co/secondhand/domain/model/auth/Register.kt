package id.co.secondhand.domain.model.auth

data class Register(
    val id: Int,
    val fullName: String,
    val email: String,
    val password: String,
    val phoneNumber: Long,
    val address: String,
    val imageUrl: String?,
    val city: String,
    val createdAt: String,
    val updatedAt: String
)