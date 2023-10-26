package com.fahrijalsyawaludin.aplikasistoryapp.data.user

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)