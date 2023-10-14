package com.leeday.capstone_1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class CheckMusclepain : AppCompatActivity() {
    private val buttonSelected = BooleanArray(8)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.musclepain_check)

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
    }

    private fun updateButtonBackground(button: ImageButton, index: Int) {
        if (buttonSelected[index]) {
            button.setBackgroundColor(Color.LTGRAY)  // 선택됐을 때의 배경색
        } else {
            button.setBackgroundColor(Color.TRANSPARENT)  // 기본 배경색
        }
    }
}