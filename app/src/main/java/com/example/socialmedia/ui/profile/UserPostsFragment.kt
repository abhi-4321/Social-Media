package com.example.socialmedia.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.R
import com.example.socialmedia.adapter.UserPostsAdapter
import com.example.socialmedia.databinding.FragmentProfileBinding
import com.example.socialmedia.databinding.FragmentUserPostsBinding
import com.example.socialmedia.model.Post
import com.example.socialmedia.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserPostsFragment : Fragment() {

    private var _binding: FragmentUserPostsBinding? = null
    private val binding: FragmentUserPostsBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPostsBinding.inflate(inflater,container,false)

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.toolbar.post {
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        val postsAdapter = UserPostsAdapter(object : UserPostsAdapter.OnItemsClick {
            override fun onLikeClick() {

            }

            override fun onCommentClick(postId: Int) {
                findNavController().navigate(UserPostsFragmentDirections.actionUserPostsFragmentToPostInFragment(postId))
            }

            override fun onShareClick(post: Post) {

            }

            override fun onRemoveClick() {

            }
        })

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postsAdapter
        }

        viewModel.getAllPostsByUser()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPostsByUserFlow.collectLatest {
                val postList = it.data ?: return@collectLatest
                postsAdapter.submitList(postList)
            }
        }

        return binding.root
    }
}