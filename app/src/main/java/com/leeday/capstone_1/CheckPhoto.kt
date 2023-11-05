package com.leeday.capstone_1

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatButton

class CheckPhoto : ComponentActivity() {

    private lateinit var apiService: ApiService
    private lateinit var globalVariable: GlobalVariable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_photo)
    }
}