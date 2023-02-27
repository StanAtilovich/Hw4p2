package com.example.hw4.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.hw4.R

import com.example.hw4.activity.NewPostFragment.Companion.textArg



class AppActivity : AppCompatActivity(R.layout.activity_app) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }


            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }
                intent.removeExtra(Intent.EXTRA_TEXT)
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
                )

         //  val textPost = it.getLongExtra("courseId", 0)
         //  if (text?.isNotBlank() != true) {
         //      return@let
         //  }
         //  intent.removeExtra(Intent.EXTRA_TEXT)
         //  findNavController(R.id.nav_host_fragment).navigate(
         //      R.id.action_feedFragment_to_newPostFragment,
         //      Bundle().apply {
         //          longArg = textPost
         //      }
         //  )

        }
    }
}