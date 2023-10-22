package com.leeday.capstone_1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiaryActivityViewPager : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var apiService: ApiService
    lateinit var globalVariable: GlobalVariable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary_activity_viewpager)
        globalVariable = getApplication() as GlobalVariable

        viewPager = findViewById(R.id.viewPager)

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        loadDiaries()
    }

    private fun loadDiaries() { // diaryId 파라미터 제거
        apiService.getDiary("Bearer " + globalVariable.accesstoken) // 함수 이름 및 파라미터 수정
            .enqueue(object : Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("API_RESPONSE", responseBody?.toString() ?: "Empty response")

                        val diaries = responseBody?.map { diaryJsonObject ->
                            DiaryData(
                                diaryJsonObject.asJsonObject["subject"].asString,
                                diaryJsonObject.asJsonObject["content"].asString,
                                diaryJsonObject.asJsonObject["emotion"].asString
                            )
                        } ?: emptyList()

                        viewPager.adapter = DiaryPagerAdapter(diaries, supportFragmentManager)
                    } else {
                        // 오류 처리
                    }
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    // 네트워크 오류 처리
                }
            })
    }
}
