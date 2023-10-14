package com.leeday.capstone_1

import com.google.gson.annotations.SerializedName


data class postQuestion(
    @SerializedName("subject")
    val subject : String,

    @SerializedName("subject")
    val content : String,

    @SerializedName("subject")
    val tag : String
)

data class Answer(
    val flushing_face: Int,
    val sweating: Int,
    val headache: Int,
    val condition: Int
)

//Frequency Check DTO
data class Symptom(
    val id: Int,
    val create_date: String,
    val flushing_face: Int,
    val sweating: Int,
    val headache: Int,
    val condition: Int,
    val user: User
)

data class User(val id: Int, val user_loginid: String, val username: String)

data class ResponseData(
    val answerValue: Int // 응답값. 실제 데이터에 따라 필드 이름 및 타입 변경이 필요합니다.
)

