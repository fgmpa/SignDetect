package com.eugene.signdetect.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugene.signdetect.domain.entity.DetectionHistory
import com.eugene.signdetect.domain.entity.DetectionResult
import com.eugene.signdetect.domain.repository.DetectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: DetectionRepository
) : ViewModel() {

    private val _historyLiveData = MutableLiveData<List<DetectionHistory>>()
    val historyLiveData: LiveData<List<DetectionHistory>> = _historyLiveData

    fun loadHistory(token: String) {
        viewModelScope.launch {
            try {
                val history = repository.getDetectionHistory(token)
                _historyLiveData.postValue(history)
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Failed to load history", e)
            }
        }
    }
}