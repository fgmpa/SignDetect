package com.eugene.signdetect.domain.usecases

import com.eugene.signdetect.domain.entity.DetectionResult
import com.eugene.signdetect.domain.repository.DetectionRepository


class DetectSignUseCase(
    private val repository: DetectionRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray): List<DetectionResult> {
        return repository.detectTrafficSign(imageBytes)
    }
}