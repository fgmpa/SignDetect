package com.eugene.signdetect.data.api

import com.eugene.signdetect.data.model.DetectionResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
interface DetectionApiService {

    @Multipart
    @POST("/detect")
    suspend fun detectTrafficSign(
        @Part file: MultipartBody.Part
    ): DetectionResponseDto
}