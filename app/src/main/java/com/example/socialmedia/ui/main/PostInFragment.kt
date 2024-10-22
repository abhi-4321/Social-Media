package com.example.socialmedia.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.adapter.CommentsAdapter
import com.example.socialmedia.databinding.FragmentPostInBinding
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


        return binding.root
    }

}