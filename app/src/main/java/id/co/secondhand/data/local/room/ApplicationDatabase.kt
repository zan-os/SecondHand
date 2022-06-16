package id.co.secondhand.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.co.secondhand.data.local.entity.UserEntity
import id.co.secondhand.data.local.room.dao.UserDao

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}