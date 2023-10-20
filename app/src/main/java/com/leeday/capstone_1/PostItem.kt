package com.leeday.capstone_1//package com.leeday.capstone_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTextView: TextView = itemView.findViewById(R.id.subject)
        val contentPreviewTextView: TextView = itemView.findViewById(R.id.content_preview)
        val authorTextView: TextView = itemView.findViewById(R.id.author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.subjectTextView.text = post.subject
        holder.contentPreviewTextView.text = post.contentPreview
        holder.authorTextView.text = post.author
    }

    override fun getItemCount(): Int = posts.size
}
