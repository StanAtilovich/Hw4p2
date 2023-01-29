package com.example.hw4


import android.os.Bundle
import android.view.View

import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hw4.adapter.OnInteractionListener
import com.example.hw4.adapter.PostAdapter

import com.example.hw4.databinding.ActivityMainBinding


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
                viewModel.sharing(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
            override fun onEdit(post: Post){
                binding.root.visibility = View.VISIBLE
                viewModel.edit(post)
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
                binding.root.visibility = View.VISIBLE
                AndroidUtils.hideKeyboard(this)

            }
        }
        binding.deleted.setOnClickListener {
            with(binding.content){
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.root.visibility = View.VISIBLE
            }
        }
    }
}













