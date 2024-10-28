package com.example.socialmedia.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.socialmedia.repository.PostRepository
import com.example.socialmedia.ui.login.LoginActivity
import com.example.socialmedia.util.PostResult
import com.example.socialmedia.util.TokenManager
import com.example.socialmedia.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserPostsFragment : Fragment() {

    private var _binding: FragmentUserPostsBinding? = null
    private val binding: FragmentUserPostsBinding get() = _binding!!
    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var postRepository: PostRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPostsBinding.inflate(inflater,container,false)

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val name = tokenManager.getSession()!!
        binding.toolbar.post {
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        val postsAdapter = UserPostsAdapter(postRepository, object : UserPostsAdapter.OnItemsClick {
            override fun onLikeClick(isSuccessful: Boolean) {
                if (isSuccessful)
                    viewModel.getAllPosts()
                else
                    Toast.makeText(requireContext(), "Couldn't connect", Toast.LENGTH_SHORT).show()
            }

            override fun onCommentClick(postId: Int) {
                findNavController().navigate(UserPostsFragmentDirections.actionUserPostsFragmentToPostInFragment(postId))
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

            override fun onRemoveClick(postId: Int) {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete post?")
                    setMessage("")
                    setPositiveButton("Yes") { dialog, _ ->
                        viewModel.deletePost(postId)
                        dialog.dismiss()
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                }.create().show()
            }
        }, name)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postsAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteCallback.collectLatest {
                if (it is PostResult.Success)
                    viewModel.getAllPostsByUser()
                else
                    Toast.makeText(requireContext(), "Couldn't connect", Toast.LENGTH_SHORT).show()
            }
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