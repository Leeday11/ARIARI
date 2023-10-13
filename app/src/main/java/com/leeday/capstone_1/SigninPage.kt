package com.leeday.capstone_1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.200.84.39/")
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
            val postData = userInfo(
                user_loginid = postUserid, password1 = postPassword1,
                password2 = postPassword2, username = postUsername,
                birthday = postBirthday
            )

            apiService.requestPostUser(postData).enqueue(object : Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>, ) {
                    Log.d("test_debug", response.toString())

                    val dialog = AlertDialog.Builder(this@SigninPage)

                    if(response.isSuccessful()){
                        dialog.setTitle("알람!")
                        dialog.setMessage("정상적으로 등록되었습니다!")
                        dialog.show()
                    } else {

                        dialog.setMessage("회원가입 실패!")
                        dialog.show()

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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
//
//        // 뷰 초기화
//        editUsername = findViewById(R.id.edit_username)
//        editPassword1 = findViewById(R.id.edit_pw)
//        editPassword2 = findViewById(R.id.edit_pw_check)
//        editNickname = findViewById(R.id.edit_nickname)
//        editBirth = findViewById(R.id.edit_birth)
//        btnCreateAccount = findViewById(R.id.btn_create_account)
//
//        btnCreateAccount.setOnClickListener {
//            val user_loginid = editUsername.text.toString()
//            val password1 = editPassword1.text.toString()
//            val password2 = editPassword2.text.toString()
//            val username = editNickname.text.toString()
//            val birthday = editBirth.text.toString()
////            val postData = postUser(
////                username = postName, password1 = postPassword1,
////                password2 = postPassword2, email = postEmail,
////                birthday = postBirthday
////            )
//
//            if (user_loginid.isEmpty() || password1.isEmpty() || username.isEmpty() || birthday.isEmpty()) {
//                Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            if (password1 != password2) {
//                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            signUp(user_loginid, password1, password2, username, birthday)
//        }
//    }
//
//    private fun signUp(user_loginid: String, password1: String, password2: String, username: String, birthday: String) {
//        api.signUp(UserInfo(user_loginid, password1, password2, username, birthday)).enqueue(object : Callback<SignUpResponse> {
//            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
//                if (response.isSuccessful) {
//                    val signUpResponse = response.body()
//                    if (signUpResponse?.success == true) {
//                        Toast.makeText(this@SigninPage, "회원가입 성공!", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this@SigninPage, signUpResponse?.message ?: "오류 발생", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this@SigninPage, "서버 오류", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
//                Toast.makeText(this@SigninPage, "네트워크 오류", Toast.LENGTH_SHORT).show()
//            }
//        })

