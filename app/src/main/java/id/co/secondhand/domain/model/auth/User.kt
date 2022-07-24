package id.co.secondhand.domain.model.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val fullName: String,
    val phoneNumber: String?,
    val address: String?,
    val imageUrl: String?,
    val city: String?,
) : Parcelable