package com.eugene.signdetect.domain.entity

import java.util.Date


data class DetectionHistory(
    val label: String,
    val time: Date?
)