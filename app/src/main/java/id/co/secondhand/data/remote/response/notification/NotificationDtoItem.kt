package id.co.secondhand.data.remote.response.notification

import com.google.gson.annotations.SerializedName
import id.co.secondhand.data.remote.response.auth.UserDto
import id.co.secondhand.data.remote.response.seller.Product
import id.co.secondhand.domain.model.notification.Notification

data class NotificationDtoItem(
    val id: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("base_price")
    val basePrice: String,
    @SerializedName("bid_price")
    val bidPrice: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("transaction_date")
    val transactionDate: String,
    val status: String,
    @SerializedName("seller_name")
    val sellerName: String,
    @SerializedName("buyer_name")
    val buyerName: String,
    @SerializedName("receiver_id")
    val receiverId: Int,
    val read: Boolean,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("Product")
    val product: Product,
    @SerializedName("User")
    val user: UserDto
)

fun NotificationDtoItem.toDomain(): Notification =
    Notification(
        id = id,
        productId = productId,
        productName = productName,
        basePrice = basePrice,
        bidPrice = bidPrice,
        imageUrl = imageUrl,
        transactionDate = transactionDate,
        status = status,
        sellerName = sellerName,
        buyerName = buyerName,
        receiverId = receiverId,
        read = read,
        createdAt = createdAt,
        updatedAt = updatedAt,
        product = product
    )