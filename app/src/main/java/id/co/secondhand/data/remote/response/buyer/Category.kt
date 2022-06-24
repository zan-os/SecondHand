package id.co.secondhand.data.remote.response.buyer


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)