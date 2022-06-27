package id.co.secondhand.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.secondhand.data.remote.MarketApi
import id.co.secondhand.data.repository.AuthRepositoryImpl
import id.co.secondhand.data.repository.BuyerRepositoryImpl
import id.co.secondhand.data.repository.SellerRepositoryImpl
import id.co.secondhand.domain.repository.AuthRepository
import id.co.secondhand.domain.repository.BuyerRepository
import id.co.secondhand.domain.repository.SellerRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(api: MarketApi): AuthRepository {
        return AuthRepositoryImpl(api)
    }

    @Provides
    fun provideMarketRepository(api: MarketApi): BuyerRepository {
        return BuyerRepositoryImpl(api)
    }

    @Provides
    fun provideSellerRepository(api: MarketApi): SellerRepository {
        return SellerRepositoryImpl(api)
    }
}