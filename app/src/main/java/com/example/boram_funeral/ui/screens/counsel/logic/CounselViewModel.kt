package com.example.boram_funeral.ui.screens.counsel.logic

import CounselingViewModel.CounselingUiState
import com.example.boram_funeral.util.AudioRecorder

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.boram_funeral.data.counsel.model.CounselRequest
import com.example.boram_funeral.data.counsel.CounselRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File



class CounselViewModel : ViewModel() {
    private var repository = CounselRepository()
    private val _uiState = MutableStateFlow(CounselViewUiState())

    val uiState = _uiState.asStateFlow()

    fun updateField(transform: (CounselViewUiState) -> CounselViewUiState) {
        _uiState.value = transform(_uiState.value)
    }
    fun clearAllInputs() {
        _uiState.value = CounselViewUiState() // 새 객체로 초기화
    }

    var isRecording by mutableStateOf(false)

    private var recorder: AudioRecorder? = null
    private var audioFile: File? = null


    // --- UI 상태 관리 ---
    data class CounselViewUiState(
        val deceasedName: String = "",
        val burialDate: String = "",
        val checkInDate: String = "",
        val checkOutDate: String = "",
        val departureDate: String = "",
        val selectedTransport: String = "",
        val selectedPriority: String = "",
        val selectedDate: String = "",
        val selectedTime: String = "",
        val selectedFuneral: String = "",
    )


    var buttonText by mutableStateOf("서버 연결")

    // 리스트 데이터
    val transportTypes = listOf("자사 이동", "타사 이동", "이송 안함")
    val priorityTypes = listOf("선택", "지도사 요청", "회원 요청")

    val funeralOption = listOf("보람의정부장례식장", "보람세민에스장례식장", "보람인천장례식장")


    // --- 비즈니스 로직 ---

    // 1. 서버 연결 테스트
    fun checkServerConnection() {
        repository.checkConnection { resultName ->
            buttonText = resultName
        }
    }

    // 2. 데이터 저장 (POST)
    fun saveData(onSuccess: () -> Unit,) {
        // 1. 현재 시점의 상태를 '스냅샷'으로 고정 (매우 중요)
        val currentState = _uiState.value

        // 2. 고정된 currentState를 사용하여 요청 객체 생성
        val requestBody = CounselRequest(
            deceasedName = currentState.deceasedName,
            burialDate = currentState.burialDate,
            checkInDate = currentState.checkInDate,
            checkOutDate = currentState.checkOutDate,
            departureDate = currentState.departureDate,
            transport = currentState.selectedTransport,
            priority = currentState.selectedPriority,
            eventTime = "${currentState.selectedDate} ${currentState.selectedTime}",
            funeralHomeName = currentState.selectedFuneral
        )

        val jsonPreview = Gson().toJson(requestBody)
        Log.d("NetworkPayload", "서버로 날아갈 JSON: $jsonPreview")

        onSuccess()
        // 실제 전송 로직 호출
        // repository.sendData(requestBody)
    }

    // 1. 녹음 시작 (이전 페이지에서 호출)
    fun startRecording(context: Context) {
        if (recorder == null) {
            recorder = AudioRecorder(context)
        }

        // 캐시 폴더에 임시 파일 생성
        val file = File(context.cacheDir, "counsel_record_${System.currentTimeMillis()}.mp3")
        this.audioFile = file

        recorder?.start(file)
        isRecording = true
    }

    // 2. 녹음 중지 (CounselScreen에서 호출)
    fun stopAndSaveAudio(context: Context, onSuccess: () -> Unit) {
        if (isRecording && recorder != null && audioFile != null) {
            recorder?.stop()
            isRecording = false
            recorder?.saveFileToPublicStorage(context, audioFile!!)
            onSuccess()
        }
    }
}