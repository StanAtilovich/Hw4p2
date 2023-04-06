package com.example.hw4.repository


import androidx.lifecycle.LiveData
import com.example.hw4.DTO.Post


interface PostRepository {
   val data : LiveData<List<Post>>

   suspend fun likedById(id: Long)
   suspend fun save(post: Post)
   suspend fun removeById(id: Long)
   suspend fun getAll()


}