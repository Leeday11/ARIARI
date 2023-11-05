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
                        val photosList = response.body()?.map { jsonElement ->
                            val jsonObject = jsonElement.asJsonObject
                            val createDate = jsonObject["create_date"].asString
                            val imagePath = jsonObject["image"].asString
                            Pair(createDate, imagePath) // Pair 객체를 만들어 List에 추가합니다.
                        } ?: emptyList<Pair<String, String>>()

                        // ViewPager의 Adapter를 설정합니다.
                        viewPager.adapter = PhotoViewPager(photosList, supportFragmentManager)
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
