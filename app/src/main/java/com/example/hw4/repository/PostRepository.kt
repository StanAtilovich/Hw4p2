package com.example.hw4.repository


import androidx.lifecycle.LiveData
import com.example.hw4.DTO.Post
import kotlinx.coroutines.flow.Flow


interface PostRepository {
   val data : Flow<List<Post>>
   suspend fun getAll()
   suspend fun save(post: Post)
   suspend fun removeById(id: Long)
   suspend fun likedById(id: Long)
   suspend fun unliked(id: Long)


   fun getNewerCount(id: Long): Flow<Int>
   suspend fun getVisible()
   suspend fun readAll()





}