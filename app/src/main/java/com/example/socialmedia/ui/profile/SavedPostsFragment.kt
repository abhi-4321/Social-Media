package com.example.socialmedia.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.adapter.SavedPostsAdapter
import com.example.socialmedia.databinding.FragmentSavedPostsBinding
import com.example.socialmedia.model.Post
import com.example.socialmedia.util.TokenManager
import com.example.socialmedia.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SavedPostsFragment : Fragment() {

    private var _binding: FragmentSavedPostsBinding? = null
    private val binding: FragmentSavedPostsBinding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedPostsBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        val adapter = SavedPostsAdapter(object : SavedPostsAdapter.OnItemsClick {
            override fun onLikeClick() {

            }

            override fun onCommentClick(postId: Int) {
                findNavController().navigate(SavedPostsFragmentDirections.actionSavedPostsFragmentToPostInFragment(postId))
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
                viewModel.deletePostId(postId)
            }
        })

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.getAllPostsByUser()

        val f1 = viewModel.allPostsByUserFlow
        val f2 = viewModel.getAllPostIds()

        viewLifecycleOwner.lifecycleScope.launch {
            f1.combine(f2) { res, list2 -> res.data?.filter { list2.contains(it.id) } }.collectLatest {
                it ?: return@collectLatest
                adapter.submitList(it)
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}