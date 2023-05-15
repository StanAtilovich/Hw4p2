package com.example.hw4.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw4.DTO.User
import com.example.hw4.auth.AppAuth
import com.example.hw4.model.FeedModelState
import com.example.hw4.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SignInViewModel   @Inject constructor(
    val auth: AppAuth,
    private val repository: AuthRepository
) : ViewModel() {

    private val _data = MutableLiveData<User>()
    val data: LiveData<User>
        get() = _data

    private val _state = MutableLiveData<FeedModelState>()
    val state: LiveData<FeedModelState>
        get() = _state

    fun loginAttempt(login: String, password: String){
        viewModelScope.launch {
            try {
                val user = repository.authUser(login,password)
                _data.value = user
            }catch (e: Exception){
                _state.postValue(FeedModelState(loginError = true))
            }
        }
    }
}