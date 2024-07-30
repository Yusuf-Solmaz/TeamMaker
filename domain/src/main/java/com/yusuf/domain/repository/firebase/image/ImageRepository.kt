package com.yusuf.domain.repository.firebase.image

import android.net.Uri

    interface ImageRepository {
        suspend fun uploadImage(uri: Uri): Result<String>

    }
