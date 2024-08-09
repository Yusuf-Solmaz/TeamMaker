package com.yusuf.data.repository.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yusuf.data.mapper.firebase_mapper.toSavedCompetitions
import com.yusuf.data.mapper.firebase_mapper.toSavedCompetitionsDto
import com.yusuf.data.remote.dto.firebase_dto.SavedCompetitionsDto
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.domain.repository.firebase.competition.CompetitionRepository
import com.yusuf.domain.util.RootResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CompetitionRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : CompetitionRepository {

    override suspend fun saveCompetitions(savedCompetitionsModel: SavedCompetitionsModel): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                val savedCompetitionId = firestore.collection("users").document(userId)
                    .collection("savedCompetitions").document().id
                val savedCompetitionInfo =
                    savedCompetitionsModel.copy(competitionId = savedCompetitionId)
                        .toSavedCompetitionsDto()
                firestore.collection("users").document(userId).collection("savedCompetitions")
                    .document(savedCompetitionId).set(savedCompetitionInfo).await()
                emit(RootResult.Success(true))
            } else {
                emit(RootResult.Error("User not found"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllSavedCompetitions(): Flow<RootResult<List<SavedCompetitionsModel>>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                val querySnapshot = firestore.collection("users").document(userId)
                    .collection("savedCompetitions").get().await()
                val savedCompetitionList = querySnapshot.documents.mapNotNull { document ->
                    val savedCompetitionListDto =
                        document.toObject(SavedCompetitionsDto::class.java)
                    savedCompetitionListDto?.toSavedCompetitions()
                }
                emit(RootResult.Success(savedCompetitionList))
            }else{
                emit(RootResult.Error("User not found"))
            }
        }catch (e:Exception){
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteSavedCompetition(competitionId: String): Flow<RootResult<Boolean>> = flow {
        emit(RootResult.Loading)
        try {
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid
            if (userId != null) {
                val competitionCollection = firestore.collection("users").document(userId).collection("savedCompetitions")
                val querySnapshot = competitionCollection.whereEqualTo("competitionId", competitionId).get().await()
                val competitionDocument = querySnapshot.documents

                if (competitionDocument.isNotEmpty()) {
                    competitionDocument.forEach { document ->
                        document.reference.delete().await()
                    }
                    emit(RootResult.Success(true))
                } else {
                    emit(RootResult.Error("Competition not found"))
                }
            } else {
                emit(RootResult.Error("User not found"))
            }
        } catch (e: Exception) {
            emit(RootResult.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)
}