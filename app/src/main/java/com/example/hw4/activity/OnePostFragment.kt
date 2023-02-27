package com.example.hw4.activity
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hw4.DTO.Post
import com.example.hw4.R
import com.example.hw4.activity.NewPostFragment.Companion.textArg
import com.example.hw4.adapter.OnInteractionListener
import com.example.hw4.adapter.PostViewHolder
import com.example.hw4.databinding.FragmentOnePostBinding
import com.example.hw4.viewModel.PostViewModel
import kotlinx.android.synthetic.main.fragment_feed.*


class OnePostFragment : Fragment() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOnePostBinding.inflate(
            inflater,
            container, false
        )
        val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
        )
        val viewHolder = PostViewHolder(binding.onePost, object : OnInteractionListener {


            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_onePostFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    })
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)

                viewModel.sharing(post.id)
            }


            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            @SuppressLint("QueryPermissionsNeeded")
            override fun onplayVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intent)
            }

        })
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == arguments?.textArg?.toLong() } ?: kotlin.run {
                findNavController().popBackStack()
                return@observe
            }
            viewHolder.bind(post)
        }

        return binding.root
    }
}