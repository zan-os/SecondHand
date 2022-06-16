package id.co.secondhand.domain.model

import id.co.secondhand.data.remote.response.Category

data class Product(
    val basePrice: Int?,
    val categories: List<Category>?,
    val createdAt: String?,
    val description: String?,
    val id: Int?,
    val imageName: String?,
    val imageUrl: String?,
    val location: String?,
    val name: String?,
    val status: String?,
    val updatedAt: String?,
    val userId: Int?
)
