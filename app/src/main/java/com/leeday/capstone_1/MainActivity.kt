package com.leeday.capstone_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.200.84.39/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService : ApiService = retrofit.create(ApiService::class.java)

        val user_loginid : EditText = findViewById(R.id.ID_input)
        val password : EditText = findViewById(R.id.PW_input)
        val btn_login : Button = findViewById(R.id.btn_login)

        val btn_signin = findViewById<Button>(R.id.btn_account)
        btn_signin.setOnClickListener {
            // 다른 액티비티로 이동하는 인텐트 생성
            val intent = Intent(this, SigninPage::class.java)
            startActivity(intent)
        }

        //테스트 버튼! 나중에 삭제해야함.
        val btn_test = findViewById<Button>(R.id.test_button)
        btn_test.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


        val dialog = AlertDialog.Builder(this@MainActivity)

        btn_login.setOnClickListener {
            apiService.requestUserLogin(user_loginid = user_loginid.text.toString(), password = password.text.toString())
                .enqueue(object : Callback<JsonObject>{
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>,
                    ) {
                        if(response.isSuccessful){
                            val result = response.body()
                            if(result != null) {
                                val accessToken = result.get("access_token")?.asString
                                val userName = result.get("user_loginid")?.asString

                                if (accessToken != null && userName != null) {
                                    val globalVariable = getApplication() as GlobalVariable
                                    globalVariable.accesstoken = accessToken
                                    globalVariable.username = userName

                                    // 로그인 성공 시 HomeActivity로 이동
                                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Log.d("LoginDebug", "Token or Username is null")
                                    dialog.setMessage("로그인 정보를 가져오는데 실패했습니다!")
                                    dialog.show()
                                }
                            } else {
                                Log.d("LoginDebug", "Response body is null")
                                dialog.setMessage("로그인 실패!")
                                dialog.show()
                            }
                        } else {
                            dialog.setMessage("로그인 실패!")
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.d("test_debug", t.message!!)
                        dialog.setMessage("통신실패!")
                        dialog.show()
                    }
                })
        }
    }
}

//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val btnLogin = findViewById<Button>(R.id.btn_login)
//        val idInput = findViewById<EditText>(R.id.ID_input)
//        val pwInput = findViewById<EditText>(R.id.PW_input)
//
//        btnLogin.setOnClickListener {
//            val id = idInput.text.toString()
//            val password = pwInput.text.toString()
//
//            if (id.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            login(id, password)
//        }
//
//        val btn_signin = findViewById<Button>(R.id.btn_account)
//        btn_signin.setOnClickListener {
//            // 다른 액티비티로 이동하는 인텐트 생성
//            val intent = Intent(this, SigninPage::class.java)
//            startActivity(intent)
//        }
//

//    }
//
//    private fun login(id: String, password: String) {
//        api.login(UserInfo(user_loginid = id, password1 = password)).enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                if (response.isSuccessful) {
//                    val loginResponse = response.body()
//                    if (loginResponse?.success == true) {
//                        Toast.makeText(this@MainActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this@MainActivity, loginResponse?.message ?: "오류 발생", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this@MainActivity, "서버 오류", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(this@MainActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}
