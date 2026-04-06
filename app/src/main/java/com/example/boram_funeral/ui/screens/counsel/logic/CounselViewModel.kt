package com.example.boram_funeral.ui.screens.counsel.logic

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.boram_funeral.data.counsel.CounselRepository
import com.example.boram_funeral.data.counsel.model.CounselRequest
import com.example.boram_funeral.util.AudioRecorder
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class CounselViewModel : ViewModel() {

    private val repository = CounselRepository()

    private val _uiState = MutableStateFlow(CounselUiState())
    val uiState = _uiState.asStateFlow()

    var isRecording by mutableStateOf(false)
        private set

    var buttonText by mutableStateOf("서버 연결")
        private set

    private var recorder: AudioRecorder? = null
    private var audioFile: File? = null

    val funeralOptions = listOf("보람의정부장례식장", "보람세민에스장례식장", "보람인천장례식장")

    fun updateField(transform: (CounselUiState) -> CounselUiState) {
        _uiState.update(transform)
    }

    fun clearAllInputs() {
        _uiState.value = CounselUiState()
    }

    fun checkServerConnection() {
        repository.checkConnection { result -> buttonText = result }
    }

    fun saveData(onSuccess: () -> Unit) {
        val state = _uiState.value
        val requestBody = CounselRequest(
            deceasedName    = state.deceasedName,
            burialDate      = state.burialDate,
            checkInDate     = state.checkInDate,
            checkOutDate    = state.checkOutDate,
            departureDate   = state.departureDate,
            transport       = state.selectedTransport,
            priority        = state.selectedPriority,
            eventTime       = "${state.selectedDate} ${state.selectedTime}",
            funeralHomeName = state.selectedFuneral,
        )
        Log.d("NetworkPayload", "서버로 날아갈 JSON: ${Gson().toJson(requestBody)}")
        onSuccess()
    }

    /** 행사 선택 또는 녹음 시작 시 호출 — 배너/요약 영역에 표시할 데이터 반영 */
    fun loadEventInfo(
        deceasedName: String,
        funeralHome: String,
        affiliate: String,
        checkInDate: String,
        departureDate: String,
        mortuary: String,
        eventType: String,
        chiefMourner: String,
        productProgress: String,
        specialNote: String,
        cardPayment: String,
        cashPayment: String,
        totalAmount: String,
    ) {
        _uiState.update {
            it.copy(
                deceasedName    = deceasedName,
                selectedFuneral = funeralHome,
                affiliate       = affiliate,
                checkInDate     = checkInDate,
                departureDate   = departureDate,
                mortuary        = mortuary,
                eventType       = eventType,
                chiefMourner    = chiefMourner,
                productProgress = productProgress,
                specialNote     = specialNote,
                cardPayment     = cardPayment,
                cashPayment     = cashPayment,
                totalAmount     = totalAmount,
            )
        }
    }

    fun startRecording(context: Context) {
        if (recorder == null) recorder = AudioRecorder(context)
        val file = File(context.cacheDir, "counsel_record_${System.currentTimeMillis()}.mp3")
        audioFile = file
        recorder?.start(file)
        isRecording = true
    }

    fun stopAndSaveAudio(context: Context, onSuccess: () -> Unit) {
        if (isRecording && recorder != null && audioFile != null) {
            recorder?.stop()
            isRecording = false
            recorder?.saveFileToPublicStorage(context, audioFile!!)
            onSuccess()
        }
    }
}
