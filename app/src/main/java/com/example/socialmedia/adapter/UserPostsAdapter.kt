package com.example.socialmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.R
import com.example.socialmedia.databinding.UserPostItemBinding
import com.example.socialmedia.model.Post
import com.example.socialmedia.repository.PostRepository
import com.example.socialmedia.util.setThrottledClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserPostsAdapter(private val postRepository: PostRepository, private val onItemsClick: OnItemsClick, private val name: String) : ListAdapter<Post, UserPostsAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UserPostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: UserPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.postedBy.text = post.created_by
            binding.textView.text = post.text

            val isLiked = post.liked_by.contains(name)
            val lc = post.liked_by.size
            binding.likeCount.text = "$lc"

            binding.like.apply {
                contentDescription = if (isLiked) {
                    setImageResource(R.drawable.heart_filled)
                    "l"
                } else {
                    setImageResource(R.drawable.heart_unfilled)
                    "ul"
                }

                setThrottledClickListener(1000) {
                    contentDescription = if (contentDescription == "ul") {
                        setImageResource(R.drawable.heart_filled)
                        binding.likeCount.text = (lc.plus(1)).toString()
                        "l"
                    } else {
                        setImageResource(R.drawable.heart_unfilled)
                        binding.likeCount.text = (lc.minus(1)).toString()
                        "ul"
                    }
                    likePost(post.id)
                }
            }

            binding.commentCount.text = post.comments.size.toString()

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

    private fun likePost(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = withContext(Dispatchers.IO) { postRepository.likePost(id) }
            onItemsClick.onLikeClick(response.isSuccessful)
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
        fun onLikeClick(isSuccessful: Boolean)
        fun onCommentClick(postId: Int)
        fun onShareClick(post: Post)
        fun onRemoveClick(postId: Int)
    }
}