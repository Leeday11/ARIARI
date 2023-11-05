
package com.leeday.capstone_1

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/user/create")
    fun requestPostUser(@Body postData : userInfo) : Call<JsonObject>

    @FormUrlEncoded
    @POST("api/user/login")
    fun requestUserLogin(@Field("username") user_loginid : String, @Field("password") password : String) : Call<JsonObject>

    //-------------------------------------------------------------------------------------------

    @POST("api/question/create")
    fun requestQuestionPost(
        @Header("Authorization") authorization : String,
        @Body postData:postQuestion) : Call<JsonObject>       //Call<ResponseBody> : output

    @GET("api/question/list")
    fun getQuestionPost(@Query("tag") tag : String) : Call<JsonObject>       //Call<ResponseBody> : output

    @GET("api/question/detail/{question_id}") // 경로는 실제 API 경로로 수정해야 합니다.
    fun getQuestionPostDetail(@Path("question_id") id: Int): Call<JsonObject>

    //-------------------------------------------------------------------------------------------

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
        @Header("Authorization") authorization : String): Call<JsonArray>

    @GET("api/physicalpain/list")
    fun getAnswers2(
        @Header("Authorization") authorization : String): Call<JsonArray> //여긴 왜 JsonArray로 해두었지...

    //-------------------------------------------------------------------------------------------

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

    //-------------------------------------------------------------------------------------------

    // 다이어리 등록하기
    @POST("api/diary/create")
    fun saveDiary(@Header("Authorization") token: String, @Body diaryData: DiaryData): Call<JsonObject>

    // 다이어리 가져오기
    @GET("api/diary/list")
    fun getDiary(@Header("Authorization") token: String): Call<JsonArray>

    //-------------------------------------------------------------------------------------------

    // 사진 등록하기
    @Multipart
    @POST("api/imdiary/create")
    fun getphoto(@Header("Authorization") token: String, @Part image: MultipartBody.Part): Call<JsonObject>


    // 사진 가져오기


}