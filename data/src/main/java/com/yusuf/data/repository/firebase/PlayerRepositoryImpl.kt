package com.yusuf.data.repository.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yusuf.data.mapper.firebase_mapper.toCompetitionData
import com.yusuf.data.mapper.firebase_mapper.toCompetitionDataDto
import com.yusuf.data.mapper.firebase_mapper.toPlayerData
import com.yusuf.data.mapper.firebase_mapper.toPlayerDataDto
import com.yusuf.data.remote.dto.firebase_dto.CompetitionDataDto
import com.yusuf.data.remote.dto.firebase_dto.PlayerDataDto
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.repository.firebase.player.PlayerRepository
import com.yusuf.domain.util.RootResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : PlayerRepository {

    override suspend fun getAllPlayers(): Flow<RootResult<List<PlayerData>>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                val querySnapshot =
                    firestore.collection("users").document(userId).collection("players").get()
                        .await()
                val playerList = querySnapshot.documents.mapNotNull { document ->
                    val playerDataDto = document.toObject(PlayerDataDto::class.java)
                    playerDataDto?.toPlayerData()
                }
                emit(RootResult.Success(playerList))
            } else {
                emit(RootResult.Error("User ID is null"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getCurrentUserId(): Flow<RootResult<String?>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            emit(RootResult.Success(userId))
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun addPlayer(
        playerData: PlayerData,
        imageUri: Uri,
        imagePathString: String
    ): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                uploadImage(imageUri, imagePathString).collect { result ->

                    Log.d("PlayerRepositoryImpl", "Image upload result: $result")
                    when (result) {
                        is RootResult.Loading -> {

                        }

                        is RootResult.Success -> {
                            val imageUrl = result.data
                            val playerInfo = imageUrl?.let {
                                playerData.copy(profilePhotoUrl = it).toPlayerDataDto()
                            }
                            if (playerInfo != null) {
                                firestore.collection("users").document(userId).collection("players")
                                    .add(playerInfo).await()
                            }
                            emit(RootResult.Success(true))
                        }

                        is RootResult.Error -> {
                            emit(RootResult.Error(result.message ?: "Image upload failed"))
                        }
                    }
                }
            } else {
                emit(RootResult.Error("User ID is null"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun addCompetition(competitionData: CompetitionData): Flow<RootResult<Boolean>> =
        flow {
            emit(RootResult.Loading)
            try {
                val currentUser = firebaseAuth.currentUser
                val userId = currentUser?.uid
                if (userId != null) {
                    val competitionId =
                        firestore.collection("users").document(userId).collection("competitions")
                            .document().id
                    val competitionInfo =
                        competitionData.copy(competitionId = competitionId).toCompetitionDataDto()
                    firestore.collection("users").document(userId).collection("competitions")
                        .document(competitionId).set(competitionInfo).await()
                    emit(RootResult.Success(true))
                } else {
                    emit(RootResult.Error("User ID is null"))
                }
            } catch (e: Exception) {
                emit(RootResult.Error(e.message ?: "Something went wrong"))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun deleteCompetition(competitionId: String): Flow<RootResult<Boolean>> =
        flow {
            emit(RootResult.Loading)
            try {
                val currentUser = firebaseAuth.currentUser
                val userId = currentUser?.uid
                if (userId != null) {
                    val competitionRef =
                        firestore.collection("users").document(userId).collection("competitions")
                            .document(competitionId)
                    competitionRef.delete().await()
                    emit(RootResult.Success(true))
                } else {
                    emit(RootResult.Error("User ID is null"))
                }
            } catch (e: Exception) {
                emit(RootResult.Error(e.message ?: "Something went wrong"))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun deletePlayerById(playerId: String): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                val playerCollection =
                    firestore.collection("users").document(userId).collection("players")
                val querySnapshot = playerCollection.whereEqualTo("id", playerId).get().await()
                val playerDocuments = querySnapshot.documents

                if (playerDocuments.isNotEmpty()) {
                    playerDocuments.forEach { document ->
                        document.reference.delete().await()
                    }
                    emit(RootResult.Success(true))
                } else {
                    emit(RootResult.Error("Player not found"))
                }
            } else {
                emit(RootResult.Error("User ID is null"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updatePlayerById(
        playerId: String,
        updatedPlayerData: PlayerData,
    ): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                val playerCollection =
                    firestore.collection("users").document(userId).collection("players")
                val querySnapshot = playerCollection.whereEqualTo("id", playerId).get().await()
                val playerDocuments = querySnapshot.documents

                if (playerDocuments.isNotEmpty()) {
                    playerDocuments.forEach { document ->
                        document.reference.set(updatedPlayerData.toPlayerDataDto()).await()
                    }
                    emit(RootResult.Success(true))
                } else {
                    emit(RootResult.Error("Player not found"))
                }
            } else {
                emit(RootResult.Error("User ID is null"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }


    override suspend fun updatePlayerImage(
        uri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            val storageRef = FirebaseStorage.getInstance().reference
            val fileName = UUID.randomUUID().toString()
            val fileRef = storageRef.child("profile_images/$fileName.jpg")

            fileRef.putFile(uri)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        onSuccess(downloadUrl.toString())
                    }.addOnFailureListener { exception ->
                        onFailure(exception)
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun uploadImage(uri: Uri,imagePathString: String): Flow<RootResult<String>> = flow {
        emit(RootResult.Loading)
        try {
            val storageRef = storage.reference.child("$imagePathString/${UUID.randomUUID()}.jpg")
            val uploadTask = storageRef.putFile(uri).await()
            val downloadUrl = storageRef.downloadUrl.await()
            emit(RootResult.Success(downloadUrl.toString()))
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Image upload failed"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateCompetition(
        competitionId: String,
        competitionData: CompetitionData
    ): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                val competitionInfo = competitionData.toCompetitionDataDto()
                val competitionRef =
                    firestore.collection("users").document(userId).collection("competitions")
                        .document(competitionId)
                competitionRef.set(competitionInfo).await()
                emit(RootResult.Success(true))
            } else {
                emit(RootResult.Error("User ID is null"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllCompetitions(): Flow<RootResult<List<CompetitionData>>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                val querySnapshot =
                    firestore.collection("users").document(userId).collection("competitions").get()
                        .await()
                val competitionList = querySnapshot.documents.mapNotNull { document ->
                    val competitionDataDto = document.toObject(CompetitionDataDto::class.java)
                    competitionDataDto?.toCompetitionData()
                }
                emit(RootResult.Success(competitionList))
            } else {
                emit(RootResult.Error("User ID is null"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getPlayersByCompetitionType(competitionType: String): Flow<RootResult<List<PlayerData>>> =
        flow {
            emit(RootResult.Loading)
            try {
                val currentUser = firebaseAuth.currentUser
                val userId = currentUser?.uid
                if (userId != null) {
                    val querySnapshot = firestore.collection("users")
                        .document(userId)
                        .collection("players")
                        .whereEqualTo("competitionType", competitionType)
                        .get()
                        .await()

                    val playerList = querySnapshot.documents.mapNotNull { document ->
                        val playerDataDto = document.toObject(PlayerDataDto::class.java)
                        playerDataDto?.toPlayerData()
                    }
                    emit(RootResult.Success(playerList))
                } else {
                    emit(RootResult.Error("User ID is null"))
                }
            } catch (e: Exception) {
                emit(RootResult.Error(e.message ?: "Something went wrong"))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun deleteCurrentUser(): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {

                val userDocRef = firestore.collection("users").document(userId)


                val playerCollection = userDocRef.collection("players").get().await()
                playerCollection.documents.forEach { it.reference.delete().await() }


                val competitionCollection = userDocRef.collection("competitions").get().await()
                competitionCollection.documents.forEach { it.reference.delete().await() }

                userDocRef.delete().await()

                currentUser.delete().await()

                emit(RootResult.Success(true))
            } else {
                emit(RootResult.Error("User ID is null"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

}