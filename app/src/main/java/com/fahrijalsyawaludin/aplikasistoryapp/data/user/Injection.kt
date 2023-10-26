package com.fahrijalsyawaludin.aplikasistoryapp.data.user

import android.content.Context
import com.fahrijalsyawaludin.aplikasistoryapp.api.ApiConf
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserRepository
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserPreference
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val api = ApiConf.getApiService()
        return UserRepository.getInstance(pref, api)
    }

}