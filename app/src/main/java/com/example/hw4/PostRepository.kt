package com.example.hw4

import androidx.lifecycle.LiveData
interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likedById(id: Long)
    fun sharing()
    fun looking()
}