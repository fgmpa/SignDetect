package com.eugene.signdetect.di

import com.eugene.signdetect.data.api.DetectionApiService
import com.eugene.signdetect.data.repository.DetectionRepositoryImpl
import com.eugene.signdetect.domain.repository.DetectionRepository
import com.eugene.signdetect.domain.usecases.DetectSignUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("  https://fgmpa.loca.lt/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDetectionApiService(retrofit: Retrofit): DetectionApiService {
        return retrofit.create(DetectionApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDetectionRepository(api: DetectionApiService): DetectionRepository {
        return DetectionRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDetectSignUseCase(repository: DetectionRepository): DetectSignUseCase {
        return DetectSignUseCase(repository)
    }
}
