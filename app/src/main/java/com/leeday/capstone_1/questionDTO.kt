package com.leeday.capstone_1

import com.google.gson.annotations.SerializedName


data class postQuestion(
    @SerializedName("subject")
    val subject : String,

    @SerializedName("content")
    val content : String,

    @SerializedName("tag")
    val tag : String
)

data class Answer(
    val flushing_face: Int,
    val sweating: Int,
    val headache: Int,
    val condition: Int
)

data class Answer2(
    val shoulder : Boolean,
    val elbow : Boolean,
    val finger : Boolean,
    val wrist : Boolean,
    val joint : Boolean,
    val knee : Boolean,
    val ankle : Boolean,
    val waist : Boolean
)

data class User(val id: Int, val user_loginid: String, val username: String)

data class ResponseData(
    val answerValue: Int // 응답값. 실제 데이터에 따라 필드 이름 및 타입 변경이 필요합니다.
)

