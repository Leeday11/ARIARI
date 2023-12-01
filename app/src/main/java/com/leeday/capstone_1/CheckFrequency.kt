package com.leeday.capstone_1

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.activity.ComponentActivity
import com.google.gson.JsonObject
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckFrequency : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frequency_check) // replace with your layout XML name

        // 전역 변수 호출
        val globalVariable = getApplication() as GlobalVariable
        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val button = findViewById<AppCompatButton>(R.id.btn_save)
        val group1 = findViewById<RadioGroup>(R.id.group1)
        val group2 = findViewById<RadioGroup>(R.id.group2)
        val group3 = findViewById<RadioGroup>(R.id.group3)
        val group4 = findViewById<RadioGroup>(R.id.group4)

        setupRadioGroupBehavior(group1)
        setupRadioGroupBehavior(group2)
        setupRadioGroupBehavior(group3)
        setupRadioGroupBehavior(group4)

        button.setOnClickListener {
            if (isAllRadioGroupsChecked(group1, group2, group3, group4)) {
                val answer = Answer(
                    getSelectedValueFromGroup(group1),
                    getSelectedValueFromGroup(group2),
                    getSelectedValueFromGroup(group3),
                    getSelectedValueFromGroup(group4)
                )
                apiService.sendAnswers("Bearer "+globalVariable.accesstoken, answer).enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.code() == 204) {
                            val result = response.body()
                            if(result != null) {
                                globalVariable.accesstoken = result.get("access_token").asString
                                globalVariable.username = result.get("username").asString
                            }
                            // 성공적으로 데이터가 전송되었을 때의 처리
                            Toast.makeText(applicationContext, "저장되었습니다! :)", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                            Toast.makeText(applicationContext, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        // 네트워크 에러 또는 기타 이유로 실패한 경우의 처리
                        Toast.makeText(applicationContext, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // 모든 라디오 버튼 그룹이 선택되지 않은 경우의 처리
                Toast.makeText(applicationContext, "Please select all answers.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //RadioGroup의 변화를 감지하는 리스너 설정
    private fun setupRadioGroupBehavior(radioGroup: RadioGroup) {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            for (i in 0 until group.childCount) {
                val radioButton = group.getChildAt(i) as RadioButton
                // 선택된 라디오 버튼색 변경
                if (radioButton.id == checkedId) {
                    radioButton.setBackgroundColor(ContextCompat.getColor(this, R.color.warm_grey)) // selected color
                } else {
                    //선택되지 않은 라디오 버튼 배경을 기본 상태로 설정
                    radioButton.background = null
                }
            }
        }
    }

    //모든 RadioGroup이 선택되었는지 확인하는 함수
    private fun isAllRadioGroupsChecked(vararg groups: RadioGroup): Boolean {
        for (group in groups) {
            val selectedRadioButtonId = group.checkedRadioButtonId
            if (selectedRadioButtonId == View.NO_ID) {
                return false
            }
        }
        return true
    }

    //선택된 라디오 버튼의 값을 반환하는 함수
    private fun getSelectedValueFromGroup(group: RadioGroup): Int {
        return findViewById<RadioButton>(group.checkedRadioButtonId).text.toString().toInt()
    }
}
