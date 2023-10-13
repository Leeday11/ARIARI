package com.leeday.capstone_1

import retrofit2.Call
import retrofit2.http.GET

interface GetService {
    @GET("/api/symton/list")
    fun getSymptonList(): Call<List<SymptonResponse>>
}