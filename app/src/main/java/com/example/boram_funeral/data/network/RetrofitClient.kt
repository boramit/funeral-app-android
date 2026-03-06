package com.example.boram_funeral.data.network

import com.example.boram_funeral.data.counsel.remote.CounselApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://app.boram.com/"

    // 1. 공통으로 사용할 Retrofit 객체 (내부에서만 접근하도록 private)
    private val retrofit: Retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 2. 상담용 서비스 (Burial/Counsel)
    val CounselApi: CounselApiService by lazy {
        retrofit.create(CounselApiService::class.java)
    }

    // 3. 회원용 서비스 (나중에 MemberApiService를 만드시면 주석을 해제하세요)
    /*
    val memberApi: MemberApiService by lazy {
        retrofit.create(MemberApiService::class.java)
    }
    */

    // 4. 로그인 서비스 (나중에 LoginApiService를 만드시면 주석을 해제하세요)
    /*
    val loginApi: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
    */
}