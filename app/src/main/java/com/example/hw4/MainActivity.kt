package com.example.hw4


import android.os.Bundle

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.example.hw4.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content

                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                )


                //   shares.setOnClickListener {
                //       post.shareCount++
                //       textShares.text = Calc.intToShareText(post.shareCount)
                //   }
                shares.setOnClickListener {
                    viewModel.sharing()
                }
                likes.setOnClickListener {
                    viewModel.like()
                }


            }

        }
    }
}








