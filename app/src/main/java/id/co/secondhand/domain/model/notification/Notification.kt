package id.co.secondhand.domain.model.notification

import android.os.Parcelable
import id.co.secondhand.data.remote.response.seller.Product
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Notification(
    val id: Int,
    val productId: Int,
    val productName: String,
    val basePrice: String,
    val bidPrice: Int,
    val imageUrl: String?,
    val transactionDate: String?,
    val status: String,
    val sellerName: String,
    val buyerName: String?,
    val receiverId: Int,
    val read: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val product: @RawValue Product
) : Parcelable