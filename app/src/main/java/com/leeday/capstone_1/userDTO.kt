package com.leeday.capstone_1

import com.google.gson.annotations.SerializedName

data class userInfo(
    @SerializedName("user_loginid")
    val user_loginid: String,

    @SerializedName("password1")
    val password1 : String,

    @SerializedName("password2")
    val password2 : String,

    @SerializedName("username")
    val username: String,

    @SerializedName("birthday")
    val birthday : String
)