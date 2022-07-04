package id.co.secondhand.data.remote.response.notification


import com.google.gson.annotations.SerializedName
import id.co.secondhand.domain.model.notification.Notification

data class NotificationDto(
    val id: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("bid_price")
    val bidPrice: Int,
    @SerializedName("transaction_date")
    val transactionDate: String,
    val status: String,
    @SerializedName("seller_name")
    val sellerName: String,
    @SerializedName("buyer_name")
    val buyerName: String,
    @SerializedName("receiver_id")
    val receiverId: Int,
    @SerializedName("image_url")
    val imageUrl: String?,
    val read: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

fun NotificationDto.toDomain(): Notification =
    Notification(
        id,
        productId,
        bidPrice,
        transactionDate,
        status,
        sellerName,
        buyerName,
        receiverId,
        imageUrl,
        read,
        createdAt,
        updatedAt
    )

