package com.yusuf.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yusuf.data.repository.firebase.AuthRepositoryImpl
import com.yusuf.data.repository.firebase.CompetitionRepositoryImpl
import com.yusuf.data.repository.firebase.ImageRepositoryImpl
import com.yusuf.data.repository.firebase.PlayerRepositoryImpl
import com.yusuf.domain.repository.firebase.auth.AuthRepository
import com.yusuf.domain.repository.firebase.competition.CompetitionRepository
import com.yusuf.domain.repository.firebase.image.ImageRepository
import com.yusuf.domain.repository.firebase.player.PlayerRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firestore)
    }

    @Provides
    @Singleton
    fun providePlayerRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): PlayerRepository {
        return PlayerRepositoryImpl(firebaseAuth, firestore, storage)
    }


    @Provides
    @Singleton
    fun provideImageRepository(
        storage: FirebaseStorage,
        firestore: FirebaseFirestore
    ): ImageRepository {
        return ImageRepositoryImpl(storage, firestore)
    }

    @Provides
    @Singleton
    fun provideCompetitionRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): CompetitionRepository {
        return CompetitionRepositoryImpl(firebaseAuth, firestore, storage)
    }

}