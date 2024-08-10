package com.yusuf.domain.use_cases.firebase_use_cases.image

import android.net.Uri
import com.yusuf.domain.repository.firebase.player.PlayerRepository
import javax.inject.Inject

class UploadImageCompetitionUseCaseUseCase @Inject constructor
    (private val repository: PlayerRepository) {
    suspend operator fun invoke(uri: Uri): Result<String> =
         repository.uploadImageCompetition(uri)
    }
