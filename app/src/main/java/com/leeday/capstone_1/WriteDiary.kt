package com.leeday.capstone_1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WriteDiary : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_diary)

        val globalVariable = getApplication() as GlobalVariable

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiService = retrofit.create(ApiService::class.java)

        val radioGroup: RadioGroup = findViewById(R.id.emotion_group)
        val subject: EditText = findViewById(R.id.write_title)
        val content: EditText = findViewById(R.id.write_text)
        val button: Button = findViewById(R.id.save_diary)

        setupRadioGroupBehavior(radioGroup)


        button.setOnClickListener {
            val diarySubject = subject.text.toString()
            val diaryContent = content.text.toString()
            val selectedId = radioGroup.checkedRadioButtonId

            if (diarySubject.isEmpty() || diaryContent.isEmpty() || selectedId == -1) {
                Toast.makeText(this, "모든 필드를 채워주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRadioButton = findViewById<RadioButton>(selectedId)
            val diaryEmotion = selectedRadioButton.text.toString()

            val diaryData = JsonObject().apply {
                addProperty("subject", diarySubject)
                addProperty("content", diaryContent)
                addProperty("emotion", diaryEmotion)
            }

            apiService.saveDiary("Bearer " + globalVariable.accesstoken, diaryData).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@WriteDiary, "일기가 성공적으로 저장되었습니다", Toast.LENGTH_SHORT).show()
                        finish() // Save 완료 후 현재 Activity 종료
                    } else {
                        Toast.makeText(this@WriteDiary, "저장에 실패하였습니다: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@WriteDiary, "네트워크 오류가 발생하였습니다", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setupRadioGroupBehavior(radioGroup: RadioGroup) {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            for (i in 0 until group.childCount) {
                val radioButton = group.getChildAt(i) as RadioButton
                if (radioButton.id == checkedId) {
                    radioButton.setBackgroundColor(ContextCompat.getColor(this, R.color.warm_grey)) // selected color
                } else {
                    radioButton.background = null
                }
            }
        }
    }
}
