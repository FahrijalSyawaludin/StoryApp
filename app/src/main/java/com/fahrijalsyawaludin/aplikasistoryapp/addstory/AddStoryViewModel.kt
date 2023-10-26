package com.fahrijalsyawaludin.aplikasistoryapp.addstory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahrijalsyawaludin.aplikasistoryapp.api.FileUploadResponse
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserModel
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private var _infoLoading = MutableLiveData<Boolean>()
    val infoLoading get() = _infoLoading

    private var _infoResponse = MutableLiveData<FileUploadResponse>()
    val infoResponse get() = _infoResponse

    fun getSession(): UserModel {
        return runBlocking { repository.getSession().first() }
    }

    fun add(imageFile: File, description: String) {
        viewModelScope.launch {
            _infoLoading.postValue(true)
            repository.addStories("Bearer " + getSession().token, imageFile, description)
                .observeForever {
                    _infoLoading.postValue(false)
                    _infoResponse.postValue(it)
                }
        }
    }

}