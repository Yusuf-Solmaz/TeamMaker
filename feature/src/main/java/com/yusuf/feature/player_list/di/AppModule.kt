package com.yusuf.feature.player_list.di

import android.content.Context
import com.yusuf.navigation.main_datastore.MainDataStore
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
    fun provideMainDataStore(@ApplicationContext context: Context): MainDataStore {
        return MainDataStore(context)
    }
}