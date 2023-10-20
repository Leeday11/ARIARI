
package com.leeday.capstone_1

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/user/create")
    fun requestPostUser(@Body postData : userInfo) : Call<JsonObject>

    @FormUrlEncoded
    @POST("api/user/login")
    fun requestUserLogin(@Field("username") user_loginid : String, @Field("password") password : String) : Call<JsonObject>

    @POST("api/question/create")
    fun requestQuestionPost(
        @Header("Authorization") authorization : String,
        @Body postData:postQuestion) : Call<JsonObject>       //Call<ResponseBody> : output

    @GET("api/question/list")
    fun getQuestionPost(@Query("tag") tag : String) : Call<JsonObject>       //Call<ResponseBody> : output

    @GET("api/question/detail/{question_id}")
    fun getQuestionPostDetail(@Path("question_id") questionId: Int): Call<JsonObject>



    @POST("api/symptom/create")
    fun sendAnswers(
        @Header("Authorization") authorization : String,
        @Body answer: Answer): Call<JsonObject>

    @POST("api/physicalpain/create")
    fun sendAnswers2(
        @Header("Authorization") authorization : String,
        @Body answer2: Answer2): Call<JsonObject>

    @GET("api/symptom/list")
    fun getAnswers(
        @Header("Authorization") authorization : String): Call<JsonObject>

    @GET("api/physicalpain/list")
    fun getAnswers2(
        @Header("Authorization") authorization : String): Call<JsonArray>
}




//interface PostService {
//
//    @POST("api/question/create")
//    fun requestQuestionPost(
//        @Header("Authorization") authorization : String,
//        @Body postData:postQuestion) : Call<ResponseBody>       //Call<ResponseBody> : output
//}
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
