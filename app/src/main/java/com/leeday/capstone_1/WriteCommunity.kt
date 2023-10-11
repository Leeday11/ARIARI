package com.leeday.capstone_1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WriteCommunity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_community)

        //global variable call
        val globalVariable = getApplication() as GlobalVariable

        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.200.84.39/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //interface가져오기
        val postService : PostService = retrofit.create(PostService::class.java)

        //xml파일에서 각 요소들 호출
//        val sub : EditText = findViewById(R.id.question_subject)
//        val con : EditText = findViewById(R.id.question_content)
//        login_tf  = findViewById(R.id.login_tf)
//        val button : Button = findViewById(R.id.request_button)
//        val cr_button : Button = findViewById(R.id.create_button)
//        val li_button : Button = findViewById(R.id.list_button)
//        val lo_button : Button = findViewById(R.id.login_button)
//        val logout_button : Button = findViewById(R.id.logout_button)

        val sub : EditText = findViewById(R.id.write_title)
        val con : EditText = findViewById(R.id.write_text)
        val button : Button = findViewById(R.id.btn_create)
        val spinner : Spinner = findViewById(R.id.category_spinner)


        button.setOnClickListener{
            val postSubject = sub.text.toString()
            val postContent = con.text.toString()
            val postTag : String = "all"
            val postData = postQuestion(subject = postSubject, content = postContent, tag = postTag)

            postService.requestQuestionPost("Bearer "+ globalVariable.accesstoken,postData).enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("test_debug", response.toString())    //response로 온 것을 string으로 변경해서 확인
                    //onResponse가 무조건 성공 X, 실패코드(3xx & 4xx 등)에도 호출 - isSuccesful() 확인이 필요

                    val dialog = AlertDialog.Builder(this@WriteCommunity)
                    if(response.isSuccessful()){
                        dialog.setTitle("알람!")
                        dialog.setMessage("정상적으로 등록되었습니다!")
                        dialog.show()
                    } else {
                        if(response.code() == 401){
                            dialog.setMessage("로그인이 필요합니다!")
                            dialog.show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    val dialog = AlertDialog.Builder(this@WriteCommunity)
                    dialog.setTitle("알람!")
                    dialog.setMessage("통신실패!")
                    dialog.show()
                    Log.d("test","tlqkf")
                }
            })
        }


    }




}


































