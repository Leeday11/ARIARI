package com.leeday.capstone_1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CheckMusclepain : AppCompatActivity() {
    private val buttonSelected = BooleanArray(8)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.musclepain_check)

        val globalVariable = getApplication() as GlobalVariable

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val imageButton1: ImageButton = findViewById(R.id.btn_wrist)
        imageButton1.setOnClickListener {
            buttonSelected[0] = !buttonSelected[0]
            updateButtonBackground(imageButton1, 0)
        }
        val imageButton2: ImageButton = findViewById(R.id.btn_finger)
        imageButton2.setOnClickListener {
            buttonSelected[1] = !buttonSelected[1]
            updateButtonBackground(imageButton2, 1)
        }
        val imageButton3: ImageButton = findViewById(R.id.btn_shoulder)
        imageButton3.setOnClickListener {
            buttonSelected[2] = !buttonSelected[2]
            updateButtonBackground(imageButton3, 2)
        }
        val imageButton4: ImageButton = findViewById(R.id.btn_elbow)
        imageButton4.setOnClickListener {
            buttonSelected[3] = !buttonSelected[3]
            updateButtonBackground(imageButton4, 3)
        }
        val imageButton5: ImageButton = findViewById(R.id.btn_waist)
        imageButton5.setOnClickListener {
            buttonSelected[4] = !buttonSelected[4]
            updateButtonBackground(imageButton5, 4)
        }
        val imageButton6: ImageButton = findViewById(R.id.btn_hipjoint)
        imageButton6.setOnClickListener {
            buttonSelected[5] = !buttonSelected[5]
            updateButtonBackground(imageButton6, 5)
        }
        val imageButton7: ImageButton = findViewById(R.id.btn_knee)
        imageButton7.setOnClickListener {
            buttonSelected[6] = !buttonSelected[6]
            updateButtonBackground(imageButton7, 6)
        }
        val imageButton8: ImageButton = findViewById(R.id.btn_ankle)
        imageButton8.setOnClickListener {
            buttonSelected[7] = !buttonSelected[7]
            updateButtonBackground(imageButton8, 7)
        }

        val button = findViewById<AppCompatButton>(R.id.btn_save)

        button.setOnClickListener {
            if (isAnyButtonSelected()) {
                val answer2 = Answer2(
                    wrist  = buttonSelected[0],
                    finger  = buttonSelected[1],
                    shoulder = buttonSelected[2],
                    elbow = buttonSelected[3],
                    waist = buttonSelected[4],
                    joint = buttonSelected[5],
                    knee = buttonSelected[6],
                    ankle = buttonSelected[7]
                )

                val selectedIndexes = buttonSelected.mapIndexed { index, isSelected ->
                    if (isSelected) index else null
                }.filterNotNull()


                apiService.sendAnswers2("Bearer " + globalVariable.accesstoken, answer2).enqueue(object :
                    Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.code() == 204) {
                            val result = response.body()

                            if(result != null) {
                                globalVariable.accesstoken = result.get("access_token").asString
                                globalVariable.username = result.get("username").asString
                            }

                            Toast.makeText(applicationContext, "저장되었습니다! :)", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                            Toast.makeText(applicationContext, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Toast.makeText(applicationContext, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(applicationContext, "Please select at least one area.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isAnyButtonSelected(): Boolean {
        return buttonSelected.any { it }
    }


    private fun updateButtonBackground(button: ImageButton, index: Int) {
        if (buttonSelected[index]) {
            button.setBackgroundColor(Color.LTGRAY)  // 선택됐을 때의 배경색
        } else {
            button.setBackgroundColor(Color.TRANSPARENT)  // 기본 배경색
        }
    }
}