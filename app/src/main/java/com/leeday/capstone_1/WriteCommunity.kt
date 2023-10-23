package com.leeday.capstone_1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WriteCommunity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_community)

        // 전역 변수 호출
        val globalVariable = getApplication() as GlobalVariable

        // Retrofit 설정
        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // 인터페이스 가져오기
        val apiService : ApiService = retrofit.create(ApiService::class.java)

        // XML에서 각 요소들 호출
        val subject : EditText = findViewById(R.id.write_title)
        val content : EditText = findViewById(R.id.write_text)
        val button : Button = findViewById(R.id.btn_create)
        val spinner : Spinner = findViewById(R.id.category_spinner)

        // 스피너에 들어갈 항목들
        val categories = arrayOf("🤍 자유 게시판", "❤ 오늘 좋은 일 있었어요", "💛 좋은 정보 나눠봐요", "💙 제 레시피 공유합니다")
        val categoryCodes = arrayOf("all", "happy", "shareinfo", "recipe")

        // 스피너에 어댑터 설정
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        button.setOnClickListener {
            val postSubject = subject.text.toString()
            val postContent = content.text.toString()
            val selectedCategoryCode = categoryCodes[spinner.selectedItemPosition]

            val postData = postQuestion(subject = postSubject, content = postContent, tag = selectedCategoryCode)

            apiService.requestQuestionPost("Bearer " + globalVariable.accesstoken, postData).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("test_debug", response.toString())

                    if (response.isSuccessful) {
                        Toast.makeText(this@WriteCommunity, "정상적으로 등록되었습니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        when(response.code()) {
                            401 -> Toast.makeText(this@WriteCommunity, "로그인이 필요합니다!", Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(this@WriteCommunity, "오류가 발생했습니다: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@WriteCommunity, "통신실패! 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    Log.d("test", "tlqkf")
                }
            })
        }

    }
}
