package com.yusuf.domain.repository.firebase.auth

import com.google.firebase.auth.FirebaseUser
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.util.RootResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Flow<RootResult<FirebaseUser>>
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Flow<RootResult<FirebaseUser>>
    fun signOut(): Flow<RootResult<Boolean>>
    fun isLoggedIn(): Flow<RootResult<Boolean>>
    fun sendPasswordResetEmail(email: String): Flow<RootResult<Boolean>>
    suspend fun signInAnonymously(): Flow<RootResult<FirebaseUser>>
}