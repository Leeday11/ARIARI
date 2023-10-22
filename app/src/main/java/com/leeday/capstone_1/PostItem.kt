package com.leeday.capstone_1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject

class PostAdapter(private var posts: Array<JsonObject>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.post_title)
        val author: TextView = itemView.findViewById(R.id.post_author)
        val preview: TextView = itemView.findViewById(R.id.post_preview)
        init {
            itemView.setOnClickListener {
                val post = posts[adapterPosition]
                val intent = Intent(itemView.context, CardviewBoard::class.java)
                intent.putExtra("POST_ID", post["id"].asInt) // JsonObject 내의 게시물 ID 필드를 가져옵니다.
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.title.text = post["subject"].asString
        holder.author.text = post["user"].asJsonObject["username"].asString
        holder.preview.text = post["content"].asString.substring(0, minOf(50, post["content"].asString.length)) + "..."
    }

    override fun getItemCount() = posts.size

    // 새로운 데이터를 설정하는 메서드 추가
    fun setPosts(newPosts: Array<JsonObject>) {
        this.posts = newPosts
        notifyDataSetChanged() // 데이터 변경 알림
    }
}
