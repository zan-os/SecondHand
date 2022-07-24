package id.co.secondhand.data.remote.request.product

import com.google.gson.annotations.SerializedName

data class BargainRequest(
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("bid_price")
    val bidPrice: String
)
