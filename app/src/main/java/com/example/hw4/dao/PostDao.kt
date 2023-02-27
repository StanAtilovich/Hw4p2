package com.example.hw4.dao


import com.example.hw4.DTO.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: Long) : Post
    fun removeById(id: Long) : List<Post>
    fun shareById(id: Long) : Post
}