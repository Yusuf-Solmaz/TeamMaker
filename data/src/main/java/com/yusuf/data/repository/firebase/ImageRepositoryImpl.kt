package com.yusuf.data.repository.firebase

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yusuf.domain.repository.firebase.image.ImageRepository
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore
) : ImageRepository {

    override suspend fun uploadImageToStorage(
        bitmap: Bitmap,
        competitionName: String
    ): Result<String> {
        return try {
            val storageRef = storage.reference.child("competitions/${UUID.randomUUID()}.jpg")
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            storageRef.putBytes(data).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}