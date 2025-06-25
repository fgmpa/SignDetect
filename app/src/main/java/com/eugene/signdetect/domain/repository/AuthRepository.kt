package com.eugene.signdetect.domain.repository

import com.eugene.signdetect.domain.entity.DetectionResult

interface AuthRepository {
    suspend fun register(username: String, password: String): String
    suspend fun login(username: String, password: String): String
}