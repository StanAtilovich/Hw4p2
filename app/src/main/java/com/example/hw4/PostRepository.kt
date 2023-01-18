package com.example.hw4

import androidx.lifecycle.LiveData
interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun sharing()
    fun looking()
}