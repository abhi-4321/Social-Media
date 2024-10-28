package com.example.socialmedia.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.R
import com.example.socialmedia.adapter.CommentsAdapter
import com.example.socialmedia.databinding.FragmentPostInBinding
import com.example.socialmedia.util.PostResult
import com.example.socialmedia.util.TokenManager
import com.example.socialmedia.util.setThrottledClickListener
import com.example.socialmedia.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PostInFragment : Fragment() {

    private var _binding: FragmentPostInBinding? = null
    private val binding: FragmentPostInBinding get() = _binding!!
    private val args: PostInFragmentArgs by navArgs()
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostInBinding.inflate(inflater,container, false)

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class]
        val adapter = CommentsAdapter()

        val name = tokenManager.getSession()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.getPostById(args.postId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postFlow.collectLatest {
                val post = it.data ?: return@collectLatest
                binding.postedBy.text = post.created_by
                binding.textView.text = post.text

                val isLiked = post.liked_by.contains(name)

                binding.like.apply {
                    contentDescription = if (isLiked) {
                        setImageResource(R.drawable.heart_filled)
                        "l"
                    } else {
                        setImageResource(R.drawable.heart_unfilled)
                        "ul"
                    }
                }
                binding.likeCount.text = "${post.liked_by.size}"
                binding.commentCount.text = "${post.comments.size}"

                adapter.submitList(post.comments)
            }
        }

        binding.like.apply{
            setThrottledClickListener(1000) {
                val lc = binding.likeCount.text.toString().toInt()
                contentDescription = if (contentDescription == "ul") {
                    setImageResource(R.drawable.heart_filled)
                    binding.likeCount.text = (lc.plus(1)).toString()
                    "l"
                } else {
                    setImageResource(R.drawable.heart_unfilled)
                    binding.likeCount.text = (lc.minus(1)).toString()
                    "ul"
                }
                likePost(args.postId,viewModel)
            }
        }

        binding.send.setOnClickListener {
            postComment(viewModel,args.postId)
            binding.txtEmail.setText("")
            binding.txtEmail.clearFocus()
        }

        return binding.root
    }

    private fun likePost(id: Int, vm: MainViewModel) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = withContext(Dispatchers.IO) { vm.likePost(id) }
            if (response.isSuccessful) {
                vm.getPostById(id)
            } else {
                Toast.makeText(requireContext(), "Couldn't connect", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postComment(vm: MainViewModel,postId: Int) {
        val comm = binding.txtEmail.text.toString()
        vm.postComment(postId,comm)

        viewLifecycleOwner.lifecycleScope.launch {
            vm.commentCallback.collectLatest {
                when(it) {
                    is PostResult.Success -> {
                        vm.getPostById(postId)
                    }
                    is PostResult.Failure -> {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                    is PostResult.Loading -> {
                    }
                }
            }
        }
    }

}