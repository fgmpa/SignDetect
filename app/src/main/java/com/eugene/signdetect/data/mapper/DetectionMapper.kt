package com.eugene.signdetect.data.mapper

import com.eugene.signdetect.data.model.DetectionDto
import com.eugene.signdetect.data.model.HistoryDto
import com.eugene.signdetect.domain.entity.DetectionHistory
import com.eugene.signdetect.domain.entity.DetectionResult
import java.text.SimpleDateFormat
import java.util.Locale

fun DetectionDto.toDomain(): DetectionResult {
    return DetectionResult(
        label = this.label,
        confidence = this.confidence,
        box =  this.box
    )
}
fun HistoryDto.toHistoryDomain(): DetectionHistory {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
    val parsedDate = try {
        format.parse(time)
    } catch (e: Exception) {
        null
    }

    return DetectionHistory(
        label = label,
        time = parsedDate
    )
}