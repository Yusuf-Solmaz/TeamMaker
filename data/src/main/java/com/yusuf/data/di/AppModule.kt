package com.yusuf.data.di

import android.app.Application
import android.content.Context
import com.yusuf.data.repository.LocationRepositoryImpl
import com.yusuf.data.repository.team.TeamBalancerRepositoryImpl
import com.yusuf.domain.repository.LocationRepository
import com.yusuf.domain.repository.team.TeamBalancerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        context: Context
    ): LocationRepository {
        return LocationRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideTeamBalancerRepository(): TeamBalancerRepository {
        return TeamBalancerRepositoryImpl()
    }
}