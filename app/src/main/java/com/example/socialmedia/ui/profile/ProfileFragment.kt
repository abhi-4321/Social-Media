package com.example.socialmedia.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.socialmedia.databinding.FragmentProfileBinding
import com.example.socialmedia.ui.login.LoginActivity
import com.example.socialmedia.util.PostResult
import com.example.socialmedia.util.TokenManager
import com.example.socialmedia.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.posts.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUserPostsFragment())
        }

        binding.toolbar.post {
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        binding.invite.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Try out this new social media app.")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.saved.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSavedPostsFragment())
        }

        binding.logout.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Logout?")
                setMessage("")
                setPositiveButton("Yes") { dialog, _ ->
                    tokenManager.deleteSession()
                    Intent(requireContext(), LoginActivity::class.java).also {
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(it)
                        requireActivity().finish()
                    }
                    dialog.dismiss()
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            }.create().show()
        }

        viewModel.getProfile()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profileFlow.collectLatest {
                when (it) {
                    is PostResult.Success -> {
                        val body = it.data!!
                        binding.email.text = body.email
                        binding.name.text = body.username
                    }

                    is PostResult.Failure -> {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is PostResult.Loading -> {

                    }
                }
            }
        }

        binding.privacyPolicy.setOnClickListener {

        }

        binding.termsConditions.setOnClickListener {

        }

        return binding.root
    }
}