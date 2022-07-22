package id.co.secondhand.data.remote.response.buyer

import com.google.gson.annotations.SerializedName
import id.co.secondhand.domain.model.buyer.Product

data class ProductDto(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("base_price")
    val basePrice: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("image_name")
    val imageName: String,
    val location: String,
    @SerializedName("user_id")
    val userId: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("Categories")
    val categories: List<Category>,
    @SerializedName("User")
    val user: User
)

fun ProductDto.toDomain() =
    Product(
        id,
        name,
        description,
        basePrice,
        imageUrl,
        imageName,
        location,
        userId,
        status,
        createdAt,
        updatedAt,
        categories,
        user
    )