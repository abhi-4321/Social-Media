package com.example.socialmedia.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.databinding.PostItemBinding
import com.example.socialmedia.model.Post

class PostsAdapter(private val onItemsClick: OnItemsClick) : ListAdapter<Post,PostsAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.postedBy.text = post.created_by
            binding.textView.text = post.text

            binding.like.setOnClickListener {  }
            binding.share.setOnClickListener {
                onItemsClick.onShareClick(post)
            }
            binding.comment.setOnClickListener {  }
            binding.more.setOnClickListener {

            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemsClick {
        fun onLikeClick()
        fun onCommentClick()
        fun onShareClick(post: Post)
        fun onMoreClick()
    }
}