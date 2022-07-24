package id.co.secondhand.data.remote.response.buyer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Int,
    val name: String
) : Parcelable