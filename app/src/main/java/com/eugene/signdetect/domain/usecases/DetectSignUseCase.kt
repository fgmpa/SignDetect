package com.eugene.signdetect.domain.usecases

import com.eugene.signdetect.domain.entity.DetectionResult
import com.eugene.signdetect.domain.repository.DetectionRepository
import javax.inject.Inject

class DetectSignUseCase @Inject constructor(
    private val repository: DetectionRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray, token:String): List<DetectionResult> {
        return repository.detectTrafficSign(imageBytes,token)
    }
}