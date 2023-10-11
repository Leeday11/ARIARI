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