package id.co.secondhand.domain.model.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val id: Int,
    val productId: Int,
    val bidPrice: Int,
    val transactionDate: String,
    val status: String,
    val sellerName: String,
    val buyerName: String,
    val receiverId: Int,
    val imageUrl: String?,
    val read: Boolean,
    val createdAt: String?,
    val updatedAt: String?
) : Parcelable