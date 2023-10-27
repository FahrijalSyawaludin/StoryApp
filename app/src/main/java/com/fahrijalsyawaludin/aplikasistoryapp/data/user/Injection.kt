package com.fahrijalsyawaludin.aplikasistoryapp.data.user

import android.content.Context
import com.fahrijalsyawaludin.aplikasistoryapp.api.ApiConf

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val api = ApiConf.getApiService()
        return UserRepository.getInstance(pref, api)
    }

}