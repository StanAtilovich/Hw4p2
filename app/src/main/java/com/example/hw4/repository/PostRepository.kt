package com.example.hw4.repository


import com.example.hw4.DTO.Post


interface PostRepository {
    fun getAll(): List<Post>
    fun likedById(id: Long):Post
    fun unlikedById(id: Long):Post
    fun sharing(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}