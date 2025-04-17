package com.eugene.signdetect.domain.entity

data class DetectionResult(
    val label: String,
    val confidence: Float,
    val box: List<Float>? = null
)