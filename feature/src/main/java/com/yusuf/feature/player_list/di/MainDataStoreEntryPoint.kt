package com.yusuf.feature.player_list.di

import com.yusuf.navigation.main_datastore.MainDataStore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MainDataStoreEntryPoint {
    val mainDataStore: MainDataStore
}