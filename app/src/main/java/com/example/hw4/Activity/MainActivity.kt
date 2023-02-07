package com.example.hw4.Activity



import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View


import android.widget.Toast
import androidx.activity.result.launch

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hw4.util.AndroidUtils
import com.example.hw4.DTO.Post

import com.example.hw4.R
import com.example.hw4.adapter.OnInteractionListener
import com.example.hw4.adapter.PostAdapter

import com.example.hw4.databinding.ActivityMainBinding
import com.example.hw4.viewModel.PostViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type= "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent,getString(R.string.chooser_share_post))
                startActivity(shareIntent)

                viewModel.sharing(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                binding.editGroup.visibility = View.VISIBLE
                viewModel.edit(post)
            }
            @SuppressLint("QueryPermissionsNeeded")
            override fun onplayVideo(post: Post){
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                if (intent.resolveActivity(packageManager)==null){
                    Snackbar.make(
                        binding.root, getString(R.string.error_no_activity_to_open_media),
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                    return
                }
            }

        }

        )
        binding.post.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.content) {
                requestFocus()
                setText(post.content)
            }
        }

        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.editGroup.visibility = View.VISIBLE
            }
        }
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

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }
        binding.fab.setOnClickListener{
            newPostLauncher.launch()
        }


    }
}













