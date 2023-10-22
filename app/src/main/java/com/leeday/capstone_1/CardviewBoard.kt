package com.leeday.capstone_1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardviewBoard : ComponentActivity() {

    private lateinit var postTitle: TextView
    private lateinit var postWriter: TextView
    private lateinit var postDate: TextView
    private lateinit var postContent: TextView
    private lateinit var commentEditText: EditText
    private lateinit var submitCommentButton: Button
    private lateinit var apiService: ApiService
    private var question_id: Int = -1
    private lateinit var commentsAdapter: CommentsAdapter
    private var commentsList = arrayOf<JsonObject>()

    var currentUsername: String? = null

    lateinit var globalVariable: GlobalVariable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_cardview)
        globalVariable = getApplication() as GlobalVariable

        commentsAdapter = CommentsAdapter(commentsList)

        val retrofit = Retrofit.Builder()
            .baseUrl(globalVariable.api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        postTitle = findViewById(R.id.postTitle)
        postWriter = findViewById(R.id.postWriter)
        postDate = findViewById(R.id.postDate)
        postContent = findViewById(R.id.postContent)
        commentEditText = findViewById(R.id.commentEditText)
        submitCommentButton = findViewById(R.id.submitCommentButton)

        question_id = intent.getIntExtra("POST_ID", -1)

        val recyclerView: RecyclerView = findViewById(R.id.commentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = commentsAdapter


        if (question_id != -1) {
            loadPostDetail(question_id)
        } else {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show()
        }

        submitCommentButton.setOnClickListener {
            val commentText = commentEditText.text.toString()

            if (commentText.isNotEmpty()) {
                val username = currentUsername ?: "기본 사용자"
                val commentData = CommentData(0, commentText, username) // id는 서버에서 생성되므로 0으로 설정
                postComment(commentData)
            } else {
                runOnUiThread {
                    Toast.makeText(this, "댓글을 입력하세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadPostDetail(postId: Int) {
        apiService.getQuestionPostDetail(postId).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let { postDetail ->
                        Log.d("API", "Post details: $postDetail")

                        // 게시글 정보 로드
                        val postTitleText = postDetail["subject"]?.asString ?: "제목 없음"
                        val userObj = postDetail["user"]?.asJsonObject
                        val postWriterText = userObj?.get("username")?.asString ?: "작성자 없음"
                        val postDateText = postDetail["create_date"]?.asString ?: "날짜 없음"
                        val postContentText = postDetail["content"]?.asString ?: "내용 없음"

                        runOnUiThread {
                            postTitle.text = postTitleText
                            postWriter.text = postWriterText
                            postDate.text = postDateText
                            postContent.text = postContentText

                        // 댓글 리스트 로드
                        val commentsArray = postDetail["answers"]?.asJsonArray
                            Log.d("API", "Fetched comments: ${commentsArray.toString()}")
                            Log.d("API", "Full API Response: ${response.body().toString()}")


                            commentsArray?.let {
                            val newComments = it.map { comment -> comment.asJsonObject }.toTypedArray()
                            commentsAdapter.setComments(newComments) // 어댑터 내부 리스트 업데이트
                        }
                        Log.d("API", "Successfully fetched comments")
                      }
                    }
                } else {
                    Log.d("API", "게시글 로딩 실패: ${response.code()}. 오류: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("API", "오류 발생: $t")
            }
        })
    }

    private fun postComment(commentData: CommentData) {
        apiService.postComment("Bearer " + globalVariable.accesstoken, question_id, commentData).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CardviewBoard, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        loadPostDetail(question_id)  // 댓글 목록을 새로고칩니다.
                        commentEditText.text.clear()  // 입력 필드를 비웁니다.

//                        // 새로운 댓글을 로컬 리스트에 추가
//                        val newComment = JsonObject()
//                        newComment.addProperty("id", 0)  // 실제 ID는 서버에서 생성되므로 임시로 0
//                        newComment.addProperty("text", commentData.content)
//                        newComment.addProperty("username", commentData.username)
//
//                        val newCommentsList = commentsList.toList() + newComment
//                        commentsList = newCommentsList.toTypedArray()
//                        commentsAdapter.setComments(newCommentsList.toTypedArray())
//                        commentsAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@CardviewBoard, "댓글 등록 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@CardviewBoard, "오류 발생: $t", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    private fun updateComment(commentData: CommentData) {
        apiService.updateComment(commentData.id, commentData).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CardviewBoard, "댓글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        loadPostDetail(question_id)  // 수정 후 댓글을 다시 로드합니다.
                    } else {
                        Toast.makeText(this@CardviewBoard, "댓글 수정 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@CardviewBoard, "오류 발생: $t", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun deleteComment(commentData: CommentData) {
        apiService.deleteComment(commentData.id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CardviewBoard, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        loadPostDetail(question_id)  // 삭제 후 댓글을 다시 로드합니다.
                    } else {
                        Toast.makeText(this@CardviewBoard, "댓글 삭제 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@CardviewBoard, "오류 발생: $t", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
