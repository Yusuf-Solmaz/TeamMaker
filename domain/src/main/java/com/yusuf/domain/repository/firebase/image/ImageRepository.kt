package com.yusuf.domain.repository.firebase.image

import android.graphics.Bitmap


    interface ImageRepository {
        suspend fun uploadImageToStorage(bitmap: Bitmap, competitionName: String): Result<String>

    }
