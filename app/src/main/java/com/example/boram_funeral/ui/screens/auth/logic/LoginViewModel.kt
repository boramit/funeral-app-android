package com.example.boram_funeral.ui.screens.auth.logic

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    var idText by mutableStateOf(prefs.getString("saved_id", "") ?: "")
    var pwText by mutableStateOf("")
    var isRememberId by mutableStateOf(prefs.getBoolean("remember_id", false))
    var errorMessage by mutableStateOf("")

    fun performLogin(onSuccess: () -> Unit) {
        if (idText == "admin" && pwText == "1234") {
            if (isRememberId) {
                prefs.edit().putString("saved_id", idText).putBoolean("remember_id", true).apply()
            } else {
                prefs.edit().remove("saved_id").putBoolean("remember_id", false).apply()
            }
            onSuccess()
        } else {
            errorMessage = "아이디 또는 비밀번호가 틀렸습니다."
        }
    }
}