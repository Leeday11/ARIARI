package com.leeday.capstone_1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.leeday.capstone_1.ApiService
import okhttp3.ResponseBody

class SigninPage : AppCompatActivity() {

//    private lateinit var editUsername: EditText
//    private lateinit var editPassword1: EditText
//    private lateinit var editPassword2: EditText
//    private lateinit var editNickname: EditText
//    private lateinit var editBirth: EditText
//    private lateinit var btnCreateAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_page)

        // 전역 변수 호출
        val globalVariable = getApplication() as GlobalVariable

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService : ApiService = retrofit.create(ApiService::class.java)

        val user_loginid : EditText = findViewById(R.id.edit_userid)
        val password1 : EditText = findViewById(R.id.edit_pw)
        val password2 : EditText = findViewById(R.id.edit_pw_check)
        val username : EditText = findViewById(R.id.edit_nickname)
        val birthday : EditText = findViewById(R.id.edit_birth)
        val button : Button = findViewById(R.id.btn_create_account)

        button.setOnClickListener {
            val postUserid = user_loginid.text.toString()
            val postPassword1 = password1.text.toString()
            val postPassword2 = password2.text.toString()
            val postUsername = username.text.toString()
            val postBirthday = birthday.text.toString()
            val postData = UserInfo(
                user_loginid = postUserid, password1 = postPassword1,
                password2 = postPassword2, username = postUsername,
                birthday = postBirthday
            )

            apiService.requestPostUser(postData).enqueue(object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>, ) {
                    Log.d("test_debug", response.toString())

                    val dialog = AlertDialog.Builder(this@SigninPage)

                    if(response.isSuccessful()){
                        dialog.setTitle("알람!")
                        dialog.setMessage("정상적으로 등록되었습니다!")
                        dialog.show()
                        finish()
                    } else {
                        dialog.setMessage("회원가입 실패!")
                        dialog.show()

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("test_debug", t.message!!)

                    val dialog = AlertDialog.Builder(this@SigninPage)
                    dialog.setTitle("알람!")
                    dialog.setMessage("통신실패!")
                    dialog.show()
                }
            })
        }
    }
}