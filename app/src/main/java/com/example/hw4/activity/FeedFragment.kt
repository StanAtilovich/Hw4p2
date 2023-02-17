package com.example.hw4.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hw4.util.AndroidUtils
import com.example.hw4.DTO.Post
import com.example.hw4.R
import com.example.hw4.adapter.OnInteractionListener
import com.example.hw4.adapter.PostAdapter
import com.example.hw4.databinding.FragmentFeedBinding
import com.example.hw4.viewModel.PostViewModel
import kotlinx.android.synthetic.main.card_post.*


class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        val binding = FragmentFeedBinding.inflate(
            inflater,
            container, false
        )

        val viewModel: PostViewModel by viewModels()
        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }

        val adapter = PostAdapter(object : OnInteractionListener {
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

            override fun onEdit(post: Post) {
                binding.editGroup.visibility = View.VISIBLE
                viewModel.edit(post)
                newPostLauncher.launch(post.content)
            }

            @SuppressLint("QueryPermissionsNeeded")
            override fun onplayVideo(post: Post) {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intent)
            }

        }

        )
        binding.post.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.content) {
                requestFocus()
                setText(post.content)
            }
        }


    //    binding.save.setOnClickListener {
    //        viewModel.changeContent(binding.edit.text.toString())
    //        viewModel.save()
    //        AndroidUtils.hideKeyboard(requireView())
    //        findNavController().navigateUp()
    //    }


        binding.deleted.setOnClickListener {
            with(binding.content) {
                viewModel.clear()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.editGroup.visibility = View.VISIBLE
            }
            return@setOnClickListener
        }


        binding.fab.setOnClickListener {

            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)

            newPostLauncher.launch(null)

        }
        return binding.root

    }
}