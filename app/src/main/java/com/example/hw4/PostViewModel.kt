package com.example.hw4

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel


class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    fun likeById(id: Long) = repository.likedById(id)
    fun sharing() = repository.sharing()
    fun looking() = repository.looking()
}