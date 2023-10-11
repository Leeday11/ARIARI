package com.leeday.capstone_1

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface PostService {

    @POST("api/question/create")
    fun requestPost(
        //header에 token정보를 같이 보내서 로그인여부를 확인!
        @Header("Authorization") authorization : String,
        @Body postData:postQuestion) : Call<ResponseBody> // output 정의

    @POST("api/user/create")
    fun requestPostUser(@Body postData : userInfo) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/user/login")
    fun requestUserLogin(@Field("username") user_loginid : String, @Field("password") password : String) : Call<JsonObject>

    @POST("api/question/create")
    fun requestQuestionPost(
        @Header("Authorization") authorization : String,
        @Body postData:postQuestion) : Call<ResponseBody>       //Call<ResponseBody> : output
}

//interface PostService {
//
//    @POST("api/question/create")
//    fun requestQuestionPost(
//        @Header("Authorization") authorization : String,
//        @Body postData:postQuestion) : Call<ResponseBody>       //Call<ResponseBody> : output
//}
//
//interface ApiService {
//    @POST("api/user/create")
//    fun signUp(@Body userInfo: UserInfo): Call<SignUpResponse>
//
//    @POST("api/user/login")  // API 경로는 실제 서버에 맞게 수정하셔야 합니다.
//    fun login(@Body userInfo: UserInfo): Call<LoginResponse>
//}
//
//data class UserInfo(
//    val user_loginid: String = "",
//    val username: String = "",
//    val password1: String = "",
//    val password2: String = "",
//    val birthday: String = ""
//)

data class SignUpResponse(val success: Boolean, val message: String)


// 추가해야 할 로그인 응답 클래스 (예시입니다)
data class LoginResponse(val success: Boolean, val message: String?, val token: String?)
