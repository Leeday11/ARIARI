package com.leeday.capstone_1

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/user/create")
    fun requestPostUser(@Body postData : userInfo) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/user/login")
    fun requestUserLogin(@Field("username") user_loginid : String, @Field("password") password : String) : Call<JsonObject>

    @POST("api/question/create")
    fun requestQuestionPost(
        @Header("Authorization") authorization : String,
        @Body postData:postQuestion) : Call<ResponseBody>       //Call<ResponseBody> : output

    @POST("api/sympton/create")
    fun sendAnswers(@Body answer: Answer): Call<String>

    @GET("/api/symton/list")
    fun getSymptonList(): Call<List<SymptonResponse>>

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
