package com.eugene.signdetect.data.repository

import android.util.Log
import com.eugene.signdetect.data.api.DetectionApiService
import com.eugene.signdetect.data.mapper.toDomain
import com.eugene.signdetect.data.mapper.toHistoryDomain
import com.eugene.signdetect.domain.entity.DetectionHistory
import com.eugene.signdetect.domain.entity.DetectionResult
import com.eugene.signdetect.domain.repository.DetectionRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DetectionRepositoryImpl(
    private val api: DetectionApiService
): DetectionRepository {
    override suspend fun detectTrafficSign(imageBytes: ByteArray,token:String): List<DetectionResult> {
        Log.d("NetworkDebug", "Sendier")

        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageBytes)
        val multipartBody = MultipartBody.Part.createFormData("file","image.png",requestBody)
        val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
        val response = api.detectTrafficSign(
            file = multipartBody,
            token = bearerToken
        )
        Log.d("NetworkDebug", "Received response: $response")
        return response.detections.map { it.toDomain() }
    }
    override suspend fun getDetectionHistory(token: String): List<DetectionHistory> {
        val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
        val response = api.getDetectionHistory(bearerToken)
        return response.history.map { it.toHistoryDomain() }
    }
}