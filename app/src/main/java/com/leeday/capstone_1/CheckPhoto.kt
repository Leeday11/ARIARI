package com.leeday.capstone_1

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.graphics.BitmapFactory


class CheckPhoto : ComponentActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textViewDate: TextView
    private lateinit var apiService: ApiService
    private lateinit var globalVariable: GlobalVariable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_photo)

        imageView = findViewById(R.id.checkPhotoView)
        textViewDate = findViewById(R.id.dateTextView)
        globalVariable = getApplication() as GlobalVariable

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        // 특정 사진 정보를 로드합니다.
        loadPhotoList()
    }

    private fun loadPhotoList() {
        apiService.getPhoto("Bearer " + globalVariable.accesstoken)
            .enqueue(object : Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        // 첫 번째 사진 정보만 가져오는 예시입니다.
                        // 필요에 따라 목록에서 특정 항목을 선택하는 로직을 추가해야 합니다.
                        val photoInfo = response.body()?.get(0)?.asJsonObject
                        val createDate = photoInfo?.get("create_date")?.asString
                        val imageName = photoInfo?.get("image")?.asString

                        textViewDate.text = createDate

                        // 이미지 이름을 사용하여 사진 파일을 로드합니다.
                        imageName?.let { loadImage(it) }
                    } else {
                        // 오류 처리
                    }
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    // 네트워크 오류 처리
                }
            })
    }

    private fun loadImage(imageName: String) {
        apiService.getPhotoFile(imageName, "Bearer " + globalVariable.accesstoken)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // InputStream을 Bitmap으로 변환합니다
                        val inputStream = response.body()?.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)

                        // UI 스레드에서 ImageView에 Bitmap을 설정합니다
                        runOnUiThread {
                            imageView.setImageBitmap(bitmap)
                        }
                    } else {
                        // 오류 처리
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 네트워크 오류 처리
                }
            })
    }



}
