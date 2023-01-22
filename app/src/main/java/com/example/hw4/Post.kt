package com.example.hw4

data class Post(
    val id:Long,
    val author:String,
    val content: String,
    val published:String,
    val likedByMe:Boolean= false,
    val likCount: Int,
    val shareByMe:Boolean= false,
    val shareCount: Int,
    val viewByMe:Boolean= false,
    val countView: Int

)


