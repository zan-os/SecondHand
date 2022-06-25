package id.co.secondhand.domain.model.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Register(
    val fullName: String,
    val email: String,
    val password: String,
    val phoneNumber: Long,
    val address: String,
    val imageUrl: File,
    val city: String,
) : Parcelable
