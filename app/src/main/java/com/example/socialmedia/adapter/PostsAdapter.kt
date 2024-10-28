package com.example.socialmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.R
import com.example.socialmedia.databinding.PostItemBinding
import com.example.socialmedia.model.Post
import com.example.socialmedia.repository.PostRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.socialmedia.util.setThrottledClickListener

class PostsAdapter(private val postRepository: PostRepository, private val onItemsClick: PostInteractionListener, private val name: String) :
    ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: PostItemBinding) :
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
            binding.save.setOnClickListener {
                onItemsClick.onSaveClick(post.id)
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

    interface PostInteractionListener {
        fun onLikeClick(isSuccessful: Boolean)
        fun onCommentClick(postId: Int)
        fun onShareClick(post: Post)
        fun onSaveClick(postId: Int)
    }
}