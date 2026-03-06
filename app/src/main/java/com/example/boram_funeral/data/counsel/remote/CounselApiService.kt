package com.example.boram_funeral.data.counsel.remote

import com.example.boram_funeral.data.counsel.model.CounselRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CounselApiService {
    @POST("LandingApi/storeboram")
    fun postBurial(@Body request: CounselRequest): Call<ResponseBody>

    @GET("LandingApi/storeboram")
    fun checkConnection(): Call<ResponseBody>
}