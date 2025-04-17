package com.eugene.signdetect.domain.repository

import com.eugene.signdetect.domain.entity.DetectionResult

interface DetectionRepository {
    suspend fun detectTrafficSign(imageBytes: ByteArray): List<DetectionResult>
}