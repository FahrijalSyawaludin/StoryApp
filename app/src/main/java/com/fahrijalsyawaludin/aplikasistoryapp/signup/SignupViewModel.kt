package com.fahrijalsyawaludin.aplikasistoryapp.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahrijalsyawaludin.aplikasistoryapp.api.RegisterResponse
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    private var _infoLoading = MutableLiveData<Boolean>()
    val infoLoading get() = _infoLoading

    private var _infoResponse = MutableLiveData<RegisterResponse>()
    val infoResponse get() = _infoResponse


    fun signup(username: String, password: String, email: String) {
        viewModelScope.launch {
            _infoLoading.postValue(true)
            repository.signup(username, password, email).observeForever {
                _infoLoading.postValue(false)
                _infoResponse.postValue(it)
            }
        }
    }

}