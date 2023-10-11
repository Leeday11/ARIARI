package com.leeday.capstone_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity

class MainGame : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_main)

        val array = findViewById<ImageButton>(R.id.btn_arraygame)
        val math = findViewById<ImageButton>(R.id.btn_mathgame)
        val card = findViewById<ImageButton>(R.id.btn_cardgame)

        array.setOnClickListener {
            val intent = Intent(this@MainGame, Unity_ArrayGame::class.java)
            startActivity(intent)
        }

        math.setOnClickListener {
            val intent = Intent(this@MainGame, Unity_MathGame::class.java)
            startActivity(intent)
        }

        card.setOnClickListener {
            val intent = Intent(this@MainGame, Unity_CardGame::class.java)
            startActivity(intent)
        }
    }
}