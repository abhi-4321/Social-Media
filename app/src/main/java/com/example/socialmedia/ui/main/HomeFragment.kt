package com.example.socialmedia.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.R
import com.example.socialmedia.adapter.PostsAdapter
import com.example.socialmedia.databinding.FragmentHomeBinding
import com.example.socialmedia.model.Post
import com.example.socialmedia.repository.PostRepository
import com.example.socialmedia.util.TokenManager
import com.example.socialmedia.viewmodel.LikeViewModel
import com.example.socialmedia.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var postRepository: PostRepository

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
        val name = tokenManager.getSession()!!
        Log.d("TokenUsername", "Home: $name")

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        viewModel.getAllPosts()

        val adapter = PostsAdapter(postRepository, object : PostsAdapter.PostInteractionListener {
            override fun onLikeClick(isSuccessful: Boolean) {
                if (isSuccessful)
                    viewModel.getAllPosts()
                else
                    Toast.makeText(requireContext(), "Couldn't connect", Toast.LENGTH_SHORT).show()
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

            override fun onSaveClick(postId: Int) {
                viewModel.savePostId(postId)
            }

        }, name)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getAllPosts()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPostsFlow.collectLatest {
                binding.swipeRefreshLayout.isRefreshing = false
                val list = it.data ?: return@collectLatest
                adapter.submitList(list)
            }
        }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddPostFragment())
        }

        return binding.root
    }

}