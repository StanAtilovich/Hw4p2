package com.example.hw4



import android.os.Bundle

import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hw4.adapter.PostAdapter

import com.example.hw4.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(
            likeClickListener = {
                viewModel.likeById(it.id)
            },
            shareClickListener = {
                viewModel.sharing(it.id)
            },
            onRemoveListener = {
                viewModel.removeById(it.id)
            }
        )
        binding.post.adapter = adapter
        viewModel.data.observe(this) { post ->
            adapter.submitList(post)
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

            }
        }
    }
}













