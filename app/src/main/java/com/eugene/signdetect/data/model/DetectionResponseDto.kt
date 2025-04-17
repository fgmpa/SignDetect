package com.eugene.signdetect.data.model



data class  DetectionDto(
    val label: String,
    val confidence: Float,
    val box: List<Float>? = null
)

data class DetectionResponseDto(
    val detections: List<DetectionDto>
)