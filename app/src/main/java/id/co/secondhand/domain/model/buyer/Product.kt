package id.co.secondhand.domain.model.buyer

import android.os.Parcelable
import id.co.secondhand.data.remote.response.buyer.Category
import id.co.secondhand.data.remote.response.buyer.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val basePrice: Int,
    val imageUrl: String,
    val imageName: String,
    val location: String,
    val userId: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val categories: List<Category>,
    val user: User?
) : Parcelable
