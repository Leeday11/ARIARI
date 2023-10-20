package com.leeday.capstone_1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BoardShareinfo : ComponentActivity() {

    private lateinit var postAdapter: PostAdapter
    private var posts = arrayOf<JsonObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_free)

        val globalVariable = getApplication() as GlobalVariable

        postAdapter = PostAdapter(posts)
        val recyclerView: RecyclerView = findViewById(R.id.free_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        loadPosts(apiService)
    }

    private fun loadPosts(apiService: ApiService) {
        apiService.getQuestionPost("shareinfo").enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.getAsJsonArray("question_list")?.let {
                        val newPosts = it.map { post -> post.asJsonObject }.toTypedArray()
                        posts = newPosts
                        postAdapter.setPosts(newPosts) // 어댑터 내부 리스트 업데이트
                        postAdapter.notifyDataSetChanged() // 데이터 변경 알림
                    }
                    Log.d("API", "Successfully fetched posts")
                } else {
                    Log.d("API", "Failed with status code: ${response.code()}. Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("API", "오류 발생: $t")
            }
        })
    }
}