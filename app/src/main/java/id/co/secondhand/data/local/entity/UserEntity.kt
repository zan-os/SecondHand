package id.co.secondhand.data.local.entity


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "fullName")
    val fullName: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String?,

    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: Long,

    @ColumnInfo(name = "createdAt")
    val createdAt: String,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: String,

    ) : Parcelable