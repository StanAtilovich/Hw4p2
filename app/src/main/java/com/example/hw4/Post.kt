package com.example.hw4

data class Post(
    val id:Long,
    val author:String,
    val content: String,
    val published:String,
    var likedByMe:Boolean= false,
    var likCount: Int,
    val shareByMe:Boolean= false,
    var shareCount: Int,
    val countView: Int

)



