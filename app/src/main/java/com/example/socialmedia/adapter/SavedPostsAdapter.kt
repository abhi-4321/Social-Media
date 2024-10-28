package com.example.socialmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.databinding.PostItemBinding
import com.example.socialmedia.databinding.SavedPostItemBinding
import com.example.socialmedia.model.Post

class SavedPostsAdapter(private val onItemsClick: OnItemsClick) : ListAdapter<Post, SavedPostsAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SavedPostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: SavedPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.postedBy.text = post.created_by
            binding.textView.text = post.text

            binding.like.setOnClickListener { }
            binding.share.setOnClickListener {
                onItemsClick.onShareClick(post)
            }
            binding.comment.setOnClickListener {
                onItemsClick.onCommentClick(post.id)
            }
            binding.remove.setOnClickListener {
                onItemsClick.onRemoveClick(post.id)
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
        fun onCommentClick(postId: Int)
        fun onShareClick(post: Post)
        fun onRemoveClick(postId: Int)
    }
}