package com.eugene.signdetect.data.model

import com.google.gson.annotations.SerializedName


data class  DetectionDto(
    val label: String,
    val confidence: Float,
    val box: List<Float>? = null
)

data class DetectionResponseDto(
    val detections: List<DetectionDto>
)
data class HistoryResponseDto(
    val history: List<HistoryDto>
)
data class RegisterRequest(val username: String, val password: String)
data class LoginRequest(val username: String, val password: String)

data class AuthResponse(
    @SerializedName("access_token")
    val token: String
)