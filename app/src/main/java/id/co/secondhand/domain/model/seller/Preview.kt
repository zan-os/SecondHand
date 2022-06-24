package id.co.secondhand.domain.model.seller

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Preview(
    val name: String,
    val categoryId: String,
    val category: String,
    val price: String,
    val description: String,
    val location: String,
    val image: File
) : Parcelable
