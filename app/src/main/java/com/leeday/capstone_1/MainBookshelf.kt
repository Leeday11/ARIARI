package com.leeday.capstone_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainBookshelf : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookshelf_main)

        val btn_writediary = findViewById<ImageButton>(R.id.write_diary)
        btn_writediary.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, WriteDiary::class.java)
            startActivity(intent)
        }

        val btn_checkdiary = findViewById<ImageButton>(R.id.check_diary)
        btn_checkdiary.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, DiaryActivityViewPager::class.java)
            startActivity(intent)
        }
    }


}