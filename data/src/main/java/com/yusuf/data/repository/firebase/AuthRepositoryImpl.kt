package com.yusuf.data.repository.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.domain.repository.firebase.auth.AuthRepository
import com.yusuf.domain.util.RootResult
import com.yusuf.data.remote.dto.firebase_dto.PlayerDataDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AuthRepository{

    private val auth = FirebaseAuth.getInstance()

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Flow<RootResult<FirebaseUser>> = flow {
        emit(RootResult.Loading)
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(RootResult.Success(result.user))
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun signUpWithEmailAndPassword(email: String, password: String): Flow<RootResult<FirebaseUser>> = flow {
        emit(RootResult.Loading)
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            user?.let {

                val userDoc = firestore.collection("users").document(user.uid)

                val playerData = PlayerDataDto(
                    profilePhotoUrl = "",
                    firstName = "",
                    lastName = "",
                    position = "",
                    skillRating = 0
                )

                userDoc.collection("players").add(playerData).await()
            }
            emit(RootResult.Success(user))
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override fun signOut(): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            firebaseAuth.signOut()
            emit(RootResult.Success(true))
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override fun isLoggedIn(): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                emit(RootResult.Success(true))
            } else {
                emit(RootResult.Success(false))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }

    override fun sendPasswordResetEmail(email: String): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(RootResult.Success(true))
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }
}
