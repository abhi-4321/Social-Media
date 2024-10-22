package com.example.socialmedia.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.R
import com.example.socialmedia.adapter.PostsAdapter
import com.example.socialmedia.databinding.FragmentHomeBinding
import com.example.socialmedia.databinding.FragmentSignUpBinding
import com.example.socialmedia.model.Post
import com.example.socialmedia.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private val args: HomeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container, false)

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val adapter = PostsAdapter(object : PostsAdapter.OnItemsClick {
            override fun onLikeClick() {
            }

            override fun onCommentClick() {
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

        /*val posts = listOf(
            Post(
                text = "I’ve been diving deep into Kotlin's new features lately, and I have to say, I’m really impressed by how intuitive and powerful this language continues to be. The recent updates bring so many exciting improvements, especially around coroutines and null safety.",
                created_by = "Alice",
                comments = listOf("Absolutely! Kotlin's evolving fast.", "I need to check out the new features!")
            ),
            Post(
                text = "Just had the best pizza in town! It was this amazing deep-dish style with tons of gooey cheese and fresh toppings. The crust was perfectly crispy, and the sauce was tangy with a hint of garlic. Definitely going back to that place soon!",
                created_by = "Bob",
                comments = listOf("Where did you get it? I need to try that!", "Sounds delicious! Pizza night, anyone?")
            ),
            Post(
                text = "Spent the day at the beach soaking up the sun and enjoying the sound of the waves crashing on the shore. There's something so calming about being by the ocean, feeling the sand between your toes and just letting your mind wander.",
                created_by = "Charlie",
                comments = listOf("Wish I was there! The beach is my happy place.", "Looks like a perfect day for some relaxation.")
            ),
            Post(
                text = "Just finished reading this incredible book on AI and machine learning. It really opened my eyes to how these technologies are shaping the future. The examples were so well explained, and now I feel like I have a much deeper understanding of AI's potential.",
                created_by = "Diana",
                comments = listOf("What book was it? I'd love to add it to my list.", "I've been meaning to dive deeper into AI too!")
            ),
            Post(
                text = "Learning Kotlin over the past few months has been an amazing journey. The language is so expressive and concise, and it’s quickly become one of my favorites for Android development. I can’t wait to build more projects using it!",
                created_by = "Eve",
                comments = listOf("Totally agree! Kotlin has been a game-changer.", "Kotlin has improved my productivity so much!")
            ),
            Post(
                text = "Tried out a new recipe for homemade lasagna today, and it turned out way better than I expected! The layers were packed with flavor, and the cheesy goodness was just perfect. I’m definitely adding this to my go-to comfort food list.",
                name = "Frank",
                comments = listOf("What did you make? I need new recipe ideas!", "Share the recipe, please! I love lasagna.")
            ),
            Post(
                text = "Started a new workout routine this week, and I’ve been feeling so much more energetic already. It’s amazing how even just a few days of consistent exercise can make a big difference. Staying motivated is key!",
                name = "Grace",
                comments = listOf("Good luck! Keep pushing through.", "Stay consistent, the results will be worth it!")
            ),
            Post(
                text = "Spending time in nature is seriously the best way to recharge. Went on a hike this weekend through some beautiful forest trails, and the fresh air and peaceful scenery made me forget all the stress from the past week.",
                name = "Hank",
                comments = listOf("Nature really does heal the soul!", "Where did you go hiking? Sounds like a great trip!")
            ),
            Post(
                text = "Watching a classic movie marathon tonight with some old favorites! I’ve got popcorn ready and a list of films that never get old no matter how many times I’ve seen them. There’s nothing better than a cozy night in with timeless cinema.",
                name = "Ivy",
                comments = listOf("What movies are you watching? I need some recommendations!", "Sounds like the perfect evening!")
            ),
            Post(
                text = "Just got my hands on a new gadget I’ve been wanting for a while now, and I can’t wait to try it out! It’s supposed to make my workflow so much easier, and I’m really excited to see if it lives up to the hype. First impressions are promising!",
                name = "Jake",
                comments = listOf("Which gadget did you get? I've been eyeing a few too.", "Let us know how it works after you try it out!")
            )
        )*/

        viewModel.getAllPosts()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPostsFlow.collectLatest {
                adapter.submitList(it)
            }
        }

        return binding.root
    }

}