
package com.leeday.capstone_1

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("api/question/detail/{question_id}") // 경로는 실제 API 경로로 수정해야 합니다.
    fun getQuestionPostDetail(@Path("question_id") id: Int): Call<JsonObject>


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

    // 댓글 등록하기
    @POST("api/answer/create/{question_id}")
    fun postComment(
        @Header("Authorization") authorization: String,
        @Path("question_id") questionId: Int,
        @Body commentData: CommentData
    ): Call<JsonObject>

    // 댓글 가져오기
    @GET("api/answer/detail/{answer_id}")
    fun getComments(@Path("answer_id") postId: Int): Call<JsonObject>

    // 댓글 수정하기
    @PUT("api/answer/update")
    fun updateComment(@Path("question_id") commentId: Int, @Body updatedComment: CommentData): Call<JsonObject>

    // 댓글 삭제하기
    @DELETE("api/answer/delete/{answer_id}")
    fun deleteComment(@Path("answer_id") commentId: Int): Call<JsonObject>

    @POST("api/diary/create")
    fun saveDiary(@Header("Authorization") token: String, @Body diaryData: JsonObject): Call<JsonObject>

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
