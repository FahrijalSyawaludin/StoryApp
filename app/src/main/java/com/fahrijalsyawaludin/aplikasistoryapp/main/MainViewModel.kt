package com.fahrijalsyawaludin.aplikasistoryapp.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahrijalsyawaludin.aplikasistoryapp.api.StoryResponse
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserModel
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private var _infoLoading = MutableLiveData<Boolean>()
    val infoLoading get() = _infoLoading

    private var _infoResponse = MutableLiveData<StoryResponse>()
    val infoResponse get() = _infoResponse

    fun getSession(): UserModel {
        return runBlocking { repository.getSession().first() }
    }

    init {
        getAllStories(repository.getToken())
    }

    fun getAllStories(token: String) {
        viewModelScope.launch {
            _infoLoading.postValue(true)
            repository.getAllStories("Bearer " + token).observeForever {
                _infoLoading.postValue(false)
                _infoResponse.postValue(it)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}