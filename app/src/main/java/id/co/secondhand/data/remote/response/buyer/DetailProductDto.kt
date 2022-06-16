package id.co.secondhand.data.remote.response.buyer


import com.google.gson.annotations.SerializedName
import id.co.secondhand.domain.model.buyer.DetailProduct

data class DetailProductDto(
    @SerializedName("base_price")
    val basePrice: Int?,
    @SerializedName("Categories")
    val categories: List<Category>?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image_name")
    val imageName: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: Int?
)

fun DetailProductDto.toDomain(): DetailProduct =
    DetailProduct(
        basePrice,
        categories,
        createdAt,
        description,
        id,
        imageName,
        imageUrl,
        location,
        name,
        status,
        updatedAt,
        userId
    )