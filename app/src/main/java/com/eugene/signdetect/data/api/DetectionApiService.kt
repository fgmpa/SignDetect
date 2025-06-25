package com.eugene.signdetect.data.api

import com.eugene.signdetect.data.model.AuthResponse
import com.eugene.signdetect.data.model.DetectionResponseDto
import com.eugene.signdetect.data.model.HistoryDto
import com.eugene.signdetect.data.model.HistoryResponseDto
import com.eugene.signdetect.data.model.LoginRequest
import com.eugene.signdetect.data.model.RegisterRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
interface DetectionApiService {
    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @Multipart
    @POST("/detect")
    suspend fun detectTrafficSign(
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String
    ): DetectionResponseDto
    @GET("history")
    suspend fun getDetectionHistory(
        @Header("Authorization") token: String
    ): HistoryResponseDto

}