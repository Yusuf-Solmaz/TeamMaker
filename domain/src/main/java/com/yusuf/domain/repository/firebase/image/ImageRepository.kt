package com.yusuf.domain.repository.firebase.image

import android.graphics.Bitmap
import android.net.Uri

    interface ImageRepository {
        suspend fun uploadImageToStorage(bitmap: Bitmap, competitionName: String): Result<String>

    }
