package com.example.hw4.dao


import com.example.hw4.DTO.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: Long)
    fun removeById(id: Long)
    fun shareById(id: Long)
}