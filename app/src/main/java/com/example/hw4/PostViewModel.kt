package com.example.hw4

import androidx.lifecycle.LiveData


class PostViewModel {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data: LiveData<Post> = repository.get()
    fun like() = repository.like()
    fun sharing() = repository.sharing()
}