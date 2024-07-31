package com.yusuf.domain.use_cases.firebase_use_cases.image

import android.graphics.Bitmap
import android.net.Uri
import com.yusuf.domain.repository.firebase.image.ImageRepository
import javax.inject.Inject

class ImageUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(bitmap: Bitmap, competitionName: String): Result<String> {
        return repository.uploadImageToStorage(bitmap, competitionName)
    }
}