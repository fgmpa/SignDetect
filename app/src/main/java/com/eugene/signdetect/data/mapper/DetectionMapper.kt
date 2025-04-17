package com.eugene.signdetect.data.mapper

import com.eugene.signdetect.data.model.DetectionDto
import com.eugene.signdetect.domain.entity.DetectionResult

fun DetectionDto.toDomain(): DetectionResult {
    return DetectionResult(
        label = this.label,
        confidence = this.confidence,
        box =  this.box
    )
}