package com.example.boram_funeral.ui.screens.member.logic

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemberViewModel : ViewModel() {

    enum class EventStatus(val label: String) {
        ONGOING("진행"),
        COMPLETED("완료"),
    }

    data class EventItem(
        val id: String,
        val deceasedName: String,
        val funeralHomeName: String,
        val burialDate: String,
        val checkInDate: String,
        val checkOutDate: String,
        val departureDate: String,
        val status: EventStatus,
    )
    data class CounselResponse(
        val result: String
    )
    private val _eventArray = MutableStateFlow<List<EventItem>>(
        listOf(
            EventItem("1", deceasedName = "김철수", funeralHomeName = "보람인천장례식장", burialDate="2024-05-21", checkInDate="2024-05-23", checkOutDate="2024-05-24", departureDate="2024-05-26", EventStatus.ONGOING),
            EventItem("2", deceasedName = "이영희", funeralHomeName = "보람세민에스장례식장", burialDate="2024-05-22", checkInDate="2024-05-29", checkOutDate="2024-05-26", departureDate="2024-05-28", EventStatus.COMPLETED),
            EventItem("3", deceasedName = "박민수", funeralHomeName = "보람의정부장례식장", burialDate="2024-05-23", checkInDate="2024-05-25", checkOutDate="2024-05-26", departureDate="2024-05-27", EventStatus.ONGOING)
        )
    )
    val eventArray: StateFlow<List<EventItem>> = _eventArray.asStateFlow()
}