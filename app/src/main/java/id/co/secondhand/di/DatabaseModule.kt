package id.co.secondhand.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.co.secondhand.data.local.room.ApplicationDatabase
import id.co.secondhand.data.local.room.dao.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ApplicationDatabase {
        return Room.databaseBuilder(context, ApplicationDatabase::class.java, "movie.db")
            .build()
    }

    @Provides
    fun provideUserDao(database: ApplicationDatabase): UserDao {
        return database.userDao()
    }
}