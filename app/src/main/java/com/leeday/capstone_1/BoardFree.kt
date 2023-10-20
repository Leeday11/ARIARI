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

class BoardFree : ComponentActivity() {

    private lateinit var postAdapter: PostAdapter
    private val posts = mutableListOf<JsonObject>()

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
        apiService.getQuestionPost().enqueue(object: Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                response.body()?.let {
                    posts.clear()
                    posts.addAll(it)
                    postAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Log.d("API", "오류 발생: $t")
            }
        })
    }
}
