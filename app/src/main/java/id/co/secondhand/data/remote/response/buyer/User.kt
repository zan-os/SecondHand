package id.co.secondhand.data.remote.response.buyer

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    @SerializedName("full_name")
    val fullName: String,
    val email: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val address: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val city: String
) : Parcelable