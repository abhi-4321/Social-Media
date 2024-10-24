package com.example.socialmedia.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.R
import com.example.socialmedia.adapter.PostsAdapter
import com.example.socialmedia.databinding.FragmentHomeBinding
import com.example.socialmedia.model.Post
import com.example.socialmedia.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.toolbar.inflateMenu(R.menu.main_menu)
        binding.toolbar.setOnMenuItemClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
            true
        }


        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        val adapter = PostsAdapter(object : PostsAdapter.OnItemsClick {
            override fun onLikeClick() {
            }

            override fun onCommentClick(postId: Int) {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToPostInFragment(
                        postId
                    )
                )
            }

            override fun onShareClick(post: Post) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.text)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            override fun onMoreClick() {
            }

        })

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.getAllPosts()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPostsFlow.collectLatest { posts ->
                Log.d("HomeFrag", "$posts")
                adapter.submitList(posts)
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddPostFragment())
        }

        return binding.root
    }

}