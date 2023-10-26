package com.fahrijalsyawaludin.aplikasistoryapp.data.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.fahrijalsyawaludin.aplikasistoryapp.api.*
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserModel
import com.fahrijalsyawaludin.aplikasistoryapp.data.user.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val api: API
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun signup(username: String, password: String, email: String) = liveData {
        try {
            val response = api.register(username, email, password)
            emit(response)
        } catch (e: Exception) {
            emit(RegisterResponse(true, e.message.toString()))
        } catch (e: HttpException) {
            emit(RegisterResponse(true, e.message.toString()))
        }
    }

    fun login(email: String, password: String) = liveData {
        try {
            val response = api.login(email, password)
            emit(response)
        } catch (e: Exception) {
            emit(LoginResponse(null, true, e.message.toString()))
        } catch (e: HttpException) {
            emit(LoginResponse(null, true, e.message.toString()))
        }
    }

    fun getAllStories(token: String) = liveData {
        try {
            val response = api.getStories(token)
            emit(response)
        } catch (e: Exception) {
            emit(StoryResponse(null, true, e.message.toString()))
        } catch (e: HttpException) {
            emit(StoryResponse(null, true, e.message.toString()))
        }
    }

    suspend fun addStories(token: String, imageFile: File, description: String) = liveData {
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = api.uploadImage(token, multipartBody, requestBody)
            emit(successResponse)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(errorResponse)
        }
    }

    fun getToken(): String {
        return runBlocking { userPreference.getSession().first().token }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            api: API
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, api)
            }.also { instance = it }
    }
}