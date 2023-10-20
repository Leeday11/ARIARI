package com.leeday.capstone_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity

class MainCommunity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_main)

        val btn_free = findViewById<ImageButton>(R.id.free_header)
        btn_free.setOnClickListener{
            val intent = Intent(this, BoardFree::class.java)
            startActivity(intent)
        }

        val btn_good = findViewById<ImageButton>(R.id.good_header)
        btn_good.setOnClickListener{
            val intent = Intent(this, BoardGoodday::class.java)
            startActivity(intent)
        }

        val btn_shareinfo = findViewById<ImageButton>(R.id.shareinfo_header)
        btn_shareinfo.setOnClickListener{
            val intent = Intent(this, BoardShareinfo::class.java)
            startActivity(intent)
        }

        val btn_recipe = findViewById<ImageButton>(R.id.recipe_header)
        btn_recipe.setOnClickListener{
            val intent = Intent(this, BoardRecipe::class.java)
            startActivity(intent)
        }

        val btn_write = findViewById<ImageButton>(R.id.btn_write)
        btn_write.setOnClickListener {
            val intent = Intent(this, WriteCommunity::class.java)
            startActivity(intent)
        }


    }
}