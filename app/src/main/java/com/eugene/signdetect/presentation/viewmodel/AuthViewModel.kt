package com.eugene.signdetect.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugene.signdetect.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authToken = MutableLiveData<String>()
    val authToken: LiveData<String> = _authToken
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val token = repository.login(username, password)
                _authToken.value = token
                _errorMessage.value = null
            } catch (e: HttpException) {
                _errorMessage.value = when (e.code()) {
                    400 -> "Неверный логин или пароль"
                    422 -> "Невалидные данные"
                    else -> "Ошибка входа: ${e.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка: ${e.localizedMessage}"
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            try {
                val token = repository.register(username, password)
                _authToken.value = token
                _errorMessage.value = null
            } catch (e: HttpException) {
                _errorMessage.value = when (e.code()) {
                    400 -> "Пользователь уже существует"
                    422 -> "Невалидные данные"
                    else -> "Ошибка регистрации: ${e.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка: ${e.localizedMessage}"
            }
        }
    }
}