package com.leeday.capstone_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        // 텍스트뷰 찾기
        val dateTextView = findViewById<TextView>(R.id.today_date)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("✨ 오늘은 yyyy년 MM월 dd일 입니다 ✨", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        // 텍스트뷰에 날짜 설정
        dateTextView.text = currentDate

        val imageButton1 = findViewById<ImageButton>(R.id.btn_healthcheck)
        imageButton1.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, MainHealthcheck::class.java)
            startActivity(intent)
        }

        val imageButton2 = findViewById<ImageButton>(R.id.btn_game)
        imageButton2.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, MainGame::class.java)
            startActivity(intent)
        }

        val imageButton3 = findViewById<ImageButton>(R.id.btn_chatbot)
        imageButton3.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, MainChatbot::class.java)
            startActivity(intent)
        }

        val imageButton4 = findViewById<ImageButton>(R.id.btn_community)
        imageButton4.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, MainCommunity::class.java)
            startActivity(intent)
        }

        val imageButton5 = findViewById<ImageButton>(R.id.btn_bookshelf)
        imageButton5.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, MainBookshelf::class.java)
            startActivity(intent)
        }
    }
}