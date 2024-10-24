package com.example.socialmedia.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentAddPostBinding
import com.example.socialmedia.model.CreatePost
import com.example.socialmedia.model.Post
import com.example.socialmedia.util.PostResult
import com.example.socialmedia.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val binding: FragmentAddPostBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.toolbar.inflateMenu(R.menu.post_menu)
        binding.toolbar.setOnMenuItemClickListener {
            createPost(viewModel)
            true
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.clear.setOnClickListener {
            binding.editText.setText("")
        }

        return binding.root
    }

    private fun createPost(vm: MainViewModel) {
        val post = CreatePost(binding.editText.text.toString())

        vm.createPost(post)

        viewLifecycleOwner.lifecycleScope.launch {
            vm.createPostCallback.collectLatest {
                when(it) {
                    is PostResult.Success -> {
                        vm.getAllPosts()
                        Toast.makeText(requireContext(), "Post Created Successfully", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
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