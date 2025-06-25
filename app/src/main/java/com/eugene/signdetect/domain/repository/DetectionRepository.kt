package com.eugene.signdetect.domain.repository

import com.eugene.signdetect.domain.entity.DetectionHistory
import com.eugene.signdetect.domain.entity.DetectionResult

interface DetectionRepository {
    suspend fun detectTrafficSign(imageBytes: ByteArray, token:String): List<DetectionResult>
    suspend fun getDetectionHistory(token: String): List<DetectionHistory>
}