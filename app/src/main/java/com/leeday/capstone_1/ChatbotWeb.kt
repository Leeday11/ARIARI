package com.leeday.capstone_1

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class ChatbotWeb : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatbot_web)

        // global 변수 호출
//        globalVariable = getApplication() as GlobalVariable

        val web: WebView = findViewById(R.id.chatbot_web)
        val url : String = "http://192.168.0.28:8000"


        web.webChromeClient = WebChromeClient()     //크롬으로!
        web.webViewClient = WebViewClient()         //현재 창에서! 자바에서는 따로 함수 만들었어야했는데 코틀린에서는 그냥~

        val webSettings = web.settings
        webSettings.javaScriptEnabled = true        //자바스크립트 활성화!
        webSettings.domStorageEnabled = true      //쿠키같은 local 저장소
        web.loadUrl(url)

    }
}
