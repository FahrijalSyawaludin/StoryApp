package com.fahrijalsyawaludin.aplikasistoryapp.welcome

import androidx.lifecycle.ViewModel
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserModel
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class WelcomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): UserModel {
        return runBlocking { repository.getSession().first() }
    }


}