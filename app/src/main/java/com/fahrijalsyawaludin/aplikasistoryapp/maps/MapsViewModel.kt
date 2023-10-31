package com.fahrijalsyawaludin.aplikasistoryapp.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahrijalsyawaludin.aplikasistoryapp.api.ListStoryItem
import com.fahrijalsyawaludin.aplikasistoryapp.api.StoryResponse
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserModel
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MapsViewModel (private val repository: UserRepository) : ViewModel() {
    private var _infoLoading = MutableLiveData<Boolean>()
    val infoLoading get() = _infoLoading

    private var _location = MutableLiveData<List<ListStoryItem?>?>()
    val locationResponse : MutableLiveData<List<ListStoryItem?>?> = _location

    fun getLocationStory(){
        viewModelScope.launch {
            _infoLoading.postValue(true)
            try {
                _location.postValue(repository.getLocationStory())
                _infoLoading.postValue(false)

            } catch (e: Exception) {
                e.printStackTrace()
                _infoLoading.postValue(false)
            }
        }
    }

}