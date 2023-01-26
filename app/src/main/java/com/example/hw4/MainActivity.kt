package com.example.hw4


import android.os.Bundle

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hw4.adapter.OnRemoveListener

import com.example.hw4.adapter.PostAdapter

import com.example.hw4.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(object : OnRemoveListener
            {
            override fun likeClickListener(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun shareClickListener(post: Post) {
                viewModel.sharing(post)
            }

            override fun onRemoveListener(post: Post) {
                viewModel.removeById(post.id)
            }

                override fun invoke(post: Post) {
                    TODO("Not yet implemented")
                }
            }
        )
        binding.post.adapter = adapter
        viewModel.data.observe(this) { post ->
            adapter.submitList(post)
        }
    }
}









