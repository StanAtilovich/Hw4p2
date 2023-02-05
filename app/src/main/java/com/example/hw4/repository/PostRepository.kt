package com.example.hw4.repository

import androidx.lifecycle.LiveData
import com.example.hw4.DTO.Post

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likedById(id: Long)
    fun sharing(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)


}