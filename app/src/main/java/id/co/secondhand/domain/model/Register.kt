package id.co.secondhand.domain.model

data class Register(
    val address: String,
    val createdAt: String? = null,
    val email: String,
    val fullName: String,
    val id: Int? = null,
    val imageUrl: String? = null,
    val password: String,
    val phoneNumber: Long,
    val updatedAt: String? = null,
    val name: String?,
    val message: String?
)
