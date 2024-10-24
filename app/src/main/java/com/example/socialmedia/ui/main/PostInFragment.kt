package com.example.socialmedia.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.adapter.CommentsAdapter
import com.example.socialmedia.databinding.FragmentPostInBinding
import com.example.socialmedia.util.PostResult
import com.example.socialmedia.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostInFragment : Fragment() {

    private var _binding: FragmentPostInBinding? = null
    private val binding: FragmentPostInBinding get() = _binding!!
    private val args: PostInFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostInBinding.inflate(inflater,container, false)

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class]
        val adapter = CommentsAdapter()

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

                adapter.submitList(post.comments)
            }
        }

        binding.send.setOnClickListener {
            postComment(viewModel,args.postId)
            binding.txtEmail.setText("")
            binding.txtEmail.clearFocus()
        }

        return binding.root
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