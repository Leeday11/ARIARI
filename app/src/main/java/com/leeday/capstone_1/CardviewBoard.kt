package com.leeday.capstone_1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardviewBoard : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_cardview)

        val globalVariable = getApplication() as GlobalVariable

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val title :TextView = findViewById(R.id.postTitle)
        val date : TextView = findViewById(R.id.postDate)
        val content : TextView = findViewById(R.id.postContent)
        val comment : RecyclerView = findViewById(R.id.commentRecyclerView)


    }
}

