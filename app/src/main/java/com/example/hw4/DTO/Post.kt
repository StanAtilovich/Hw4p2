package com.example.hw4.DTO

import android.provider.MediaStore.Video

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likCount: Int,
    val shareByMe: Boolean = false,
    val shareCount: Int,
    val viewByMe: Boolean = false,
    val countView: Int,
    //val video: String

)





