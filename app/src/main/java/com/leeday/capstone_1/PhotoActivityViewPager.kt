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

class PhotoActivityViewPager : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var apiService: ApiService
    lateinit var globalVariable: GlobalVariable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_activity_viewpager)
        globalVariable = getApplication() as GlobalVariable

        viewPager = findViewById(R.id.viewPager)

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        loadPhotos()
    }

    private fun loadPhotos() {
        apiService.getPhoto("Bearer " + globalVariable.accesstoken)
            .enqueue(object : Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        // JSON 응답을 파싱하여 'create_date'와 'image' 정보를 가져옵니다.
                        val photos = response.body()?.map { jsonElement ->
                            val jsonObject = jsonElement.asJsonObject
                            val createDate = jsonObject["create_date"].asString
                            val imagePath = jsonObject["image"].asString
                            // 각 사진의 createDate와 imagePath를 이용하여 UI를 업데이트하는 로직을 여기에 구현합니다.
                            // 이 예시에서는 간단히 이 데이터를 로그로 출력하고 있습니다.
                            Log.d("PHOTO_INFO", "Date: $createDate, Image Path: $imagePath")
                            // TODO: imagePath로 이미지를 로딩하고 createDate를 표시하는 로직 추가
                        } ?: emptyList()

                        // 여기에서 ViewPager의 Adapter를 설정하는 등 UI 업데이트를 수행합니다.
                        // viewPager.adapter = ...

                    } else {
                        Log.e("API_ERROR", "Response not successful")
                        // 오류 처리
                    }
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Log.e("API_ERROR", "Call failed with message: ${t.message}")
                    // 네트워크 오류 처리
                }
            })
    }
}
