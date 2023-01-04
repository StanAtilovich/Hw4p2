package com.example.hw4

data class Post(
    val id:Long,
    val author:String,
    val content: String,
    val published:String,
    var likedByMe:Boolean= false,
    var likCount: Int,
    var shareByMe:Boolean= false,
    var shareCount: Int

)



