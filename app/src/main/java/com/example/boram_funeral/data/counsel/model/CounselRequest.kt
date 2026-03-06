package com.example.boram_funeral.data.counsel.model
import com.google.gson.annotations.SerializedName

data class CounselRequest(
    @SerializedName("deceased_name") val deceasedName: String,
    @SerializedName("burial_date") val burialDate: String,
    @SerializedName("check_in_date") val checkInDate: String,
    @SerializedName("check_out_date") val checkOutDate: String,
    @SerializedName("departure_date") val departureDate: String,
    @SerializedName("transport_type") val transport: String,
    @SerializedName("priority_type") val priority: String,
    @SerializedName("event_time") val eventTime: String,
    @SerializedName("funeralHomeName") val funeralHomeName: String

)