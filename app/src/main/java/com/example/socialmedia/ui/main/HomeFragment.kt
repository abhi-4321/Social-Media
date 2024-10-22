package com.example.socialmedia.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.profile)
                    findNavController().navigate(HomeFragmentDirections)
                return true
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

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
                adapter.submitList(posts)
            }
        }

        return binding.root
    }

}