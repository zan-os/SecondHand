package id.co.secondhand.data.remote.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class OrderDtoItem(
    @SerializedName("buyer_id")
    val buyerId: Int?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("Product")
    val product: Product?,
    @SerializedName("product_id")
    val productId: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("User")
    val userDto: @RawValue UserDto?
): Parcelable