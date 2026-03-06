package com.example.boram_funeral.data.counsel

import android.util.Log
import com.example.boram_funeral.data.counsel.model.CounselRequest
import com.example.boram_funeral.data.network.RetrofitClient
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CounselRepository {
    private val api = RetrofitClient.CounselApi

    fun sendData(request: CounselRequest) {
        Log.d("API_TEST", "전송 시작: ${Gson().toJson(request)}")

        api.postBurial(request).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("API_TEST", "성공 코드: ${response.code()}")
                } else {
                    // 서버 에러 내용 확인
                    Log.e("API_TEST", "응답 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API_TEST", "네트워크 에러: ${t.message}")
            }
        })
    }

    fun checkConnection(onResult: (String) -> Unit) {
        api.checkConnection().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonString = response.body()?.string()
                    // Gson을 이용해 "name" 값만 추출 (단순하게 추출하거나 DTO 사용)
                    val name = try {
                        JSONObject(jsonString).getString("name")
                    } catch (e: Exception) {
                        "성공(데이터 없음)"
                    }
                    onResult(name) // 화면(UI)으로 이름 전달
                } else {
                    onResult("에러 발생: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResult("연결 실패")
            }
        })
    }
}