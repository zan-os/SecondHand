package id.co.secondhand.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.secondhand.data.local.room.dao.UserDao
import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.repository.AuthRepositoryImpl
import id.co.secondhand.data.repository.MarketRepositoryImpl
import id.co.secondhand.domain.repository.AuthRepository
import id.co.secondhand.domain.repository.MarketRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(api: MarketApi, userDao: UserDao): AuthRepository {
        return AuthRepositoryImpl(userDao, api)
    }

    @Provides
    fun provideMarketRepository(api: MarketApi): MarketRepository {
        return MarketRepositoryImpl(api)
    }
}