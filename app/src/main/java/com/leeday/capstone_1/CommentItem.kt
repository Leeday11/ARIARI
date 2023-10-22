package com.leeday.capstone_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject

class CommentsAdapter(private var comments: Array<JsonObject>) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val c_writer: TextView = itemView.findViewById(R.id.comment_writer)
        val c_content: TextView = itemView.findViewById(R.id.comment_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.c_writer.text = comment["user"].asJsonObject["username"].asString
        holder.c_content.text = comment["content"].asString
    }

    override fun getItemCount() = comments.size

    // 새로운 데이터를 설정하는 메서드 추가
    fun setComments(newComments: Array<JsonObject>) {
        this.comments = newComments
        notifyDataSetChanged() // 데이터 변경 알림
    }
}
