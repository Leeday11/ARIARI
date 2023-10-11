package com.leeday.capstone_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity

class MainCommunity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_main)

        val btn_write = findViewById<ImageButton>(R.id.btn_write)
        btn_write.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, WriteCommunity::class.java)
            startActivity(intent)
        }


    }
}