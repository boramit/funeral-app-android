package com.example.boram_funeral.ui.screens.auth.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var idText by mutableStateOf("")
    var pwText by mutableStateOf("")
    var errorMessage by mutableStateOf("")

    fun performLogin(onSuccess: () -> Unit) {
        if (idText == "admin" && pwText == "1234") {
            onSuccess() // 성공 리모컨 실행
        } else {
            errorMessage = "아이디 또는 비밀번호가 틀렸습니다."
        }
    }
}