package com.eugene.signdetect.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugene.signdetect.domain.entity.DetectionResult
import com.eugene.signdetect.domain.usecases.DetectSignUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val detectSignUseCase: DetectSignUseCase
) : ViewModel() {

    private val _detectionResult = MutableLiveData<List<DetectionResult>>()
    val detectionResult: LiveData<List<DetectionResult>> = _detectionResult

    fun detect(imageBytes: ByteArray, token: String) {
        viewModelScope.launch {
            try {
                if (token.isBlank()) {
                    Log.e("NetworkDebug", "No token provided")
                    return@launch
                }
                Log.d("NetworkDebug", "Detecting with token")
                val results = detectSignUseCase(imageBytes, token)
                _detectionResult.postValue(results)
            } catch (e: Exception) {
                Log.e("NetworkDebug", "Detection error", e)
            }
        }
    }

}