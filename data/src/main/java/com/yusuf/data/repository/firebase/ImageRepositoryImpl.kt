package com.yusuf.data.repository.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.yusuf.domain.repository.firebase.image.ImageRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage
) : ImageRepository {

    override suspend fun uploadImage(uri: Uri): Result<String> {
        return try {
            val storageRef = storage.reference.child("images/${uri.lastPathSegment}")
            storageRef.putFile(uri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}