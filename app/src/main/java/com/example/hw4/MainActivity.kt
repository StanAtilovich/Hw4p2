package com.example.hw4


import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hw4.adapter.PostAdapter

import com.example.hw4.databinding.ActivityMainBinding
import com.example.hw4.databinding.CardPostBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter {
            viewModel.likeById(it.id)
        }
        binding.post.adapter = adapter
        viewModel.data.observe(this) { post ->
            adapter.submitList(post)
        }
    }
}









