package com.netmarble.bn.core.repository.di

import android.content.Context
import com.netmarble.bn.core.audio.BgmPlayer
import com.netmarble.bn.core.data.PhysicsRepositoryImpl
import com.netmarble.bn.core.data.ShopRepositoryImpl
import com.netmarble.bn.core.data.StatsRepositoryImpl
import com.netmarble.bn.core.data.WalletRepositoryImpl
import com.netmarble.bn.core.domain.repository.IslandRepository
import com.netmarble.bn.core.domain.repository.PhysicsRepository
import com.netmarble.bn.core.domain.repository.ShopRepository
import com.netmarble.bn.core.domain.repository.StatsRepository
import com.netmarble.bn.core.domain.repository.WalletRepository
import com.netmarble.bn.core.domain.service.PlinkoEngine
import com.netmarble.bn.core.repository.StaticIslandRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideIslandRepository(): IslandRepository = StaticIslandRepository()
    @Provides
    @Singleton
    fun provideStatsRepository(
        @ApplicationContext context: Context
    ): StatsRepository = StatsRepositoryImpl(context)
    @Provides
    @Singleton
    fun providePhysicsRepository(): PhysicsRepository = PhysicsRepositoryImpl()

    @Provides
    @Singleton
    fun providePlinkoEngine(): PlinkoEngine = PlinkoEngine()

    @Provides @Singleton
    fun provideBgmPlayer(@ApplicationContext context: Context): BgmPlayer =
        BgmPlayer(context)
}

@Module
@InstallIn(SingletonComponent::class)
object ShopModule {

    @Provides @Singleton
    fun provideWallet(@ApplicationContext context: Context): WalletRepository =
        WalletRepositoryImpl(context)

    @Provides @Singleton
    fun provideShop(@ApplicationContext context: Context, wallet: WalletRepository): ShopRepository =
        ShopRepositoryImpl(context, wallet)
}