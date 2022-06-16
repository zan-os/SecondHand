package id.co.secondhand.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import id.co.secondhand.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun saveUserData(user: UserEntity): Long
}