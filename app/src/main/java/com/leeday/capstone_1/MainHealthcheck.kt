package com.leeday.capstone_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainHealthcheck : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.healthcheck_main)

        val imageButton1 = findViewById<ImageButton>(R.id.btn_frequency)
        imageButton1.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this@MainHealthcheck, CheckFrequency::class.java)
            startActivity(intent)
        }

        val imageButton2 = findViewById<ImageButton>(R.id.btn_muscle)
        imageButton2.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this@MainHealthcheck, CheckMusclepain::class.java)
            startActivity(intent)
        }

        val imageButton3 = findViewById<ImageButton>(R.id.btn_graph)
        imageButton3.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this@MainHealthcheck, CheckGraph::class.java)
            startActivity(intent)
        }
    }
}