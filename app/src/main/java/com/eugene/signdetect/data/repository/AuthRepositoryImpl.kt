package com.eugene.signdetect.data.repository

import com.eugene.signdetect.data.api.DetectionApiService
import com.eugene.signdetect.data.model.LoginRequest
import com.eugene.signdetect.data.model.RegisterRequest
import com.eugene.signdetect.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: DetectionApiService
) : AuthRepository {

    override suspend fun register(username: String, password: String): String {
        val response = api.register(RegisterRequest(username, password))
        return response.token
    }

    override suspend fun login(username: String, password: String): String {
        val response = api.login(LoginRequest(username, password))
        return response.token
    }
}