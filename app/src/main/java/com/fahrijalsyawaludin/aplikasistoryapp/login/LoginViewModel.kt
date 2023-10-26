package com.fahrijalsyawaludin.aplikasistoryapp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahrijalsyawaludin.aplikasistoryapp.api.LoginResponse
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserModel
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private var _infoLoading = MutableLiveData<Boolean>()
    val infoLoading get() = _infoLoading

    private var _infoResponse = MutableLiveData<LoginResponse>()
    val infoResponse get() = _infoResponse

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)

        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _infoLoading.postValue(true)
            repository.login(email, password).observeForever {
                _infoLoading.postValue(false)
                _infoResponse.postValue(it)
            }
        }
    }
}