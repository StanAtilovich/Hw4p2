package com.example.hw4.repository


import androidx.lifecycle.map
import com.example.hw4.DTO.Post
import com.example.hw4.api.PostsApi
import com.example.hw4.dao.PostDao
import com.example.hw4.entity.PostEntity
import com.example.hw4.entity.toDto
import com.example.hw4.entity.toEntity
import com.example.hw4.error.ApiException
import com.example.hw4.error.NetworkException
import com.example.hw4.error.UnknownException
import java.io.IOException


class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll().map { it.toDto() }


    override suspend fun likedById(id: Long) {
        try {
            dao.likeById(id)
            val response = PostsApi.retrofitService.likedById(id)
            if (!response.isSuccessful){
                throw ApiException(response.code(), response.message())
            }
        }catch (e: IOException){
            throw NetworkException
        }catch (e: Exception){
            throw UnknownException
        }
    }

    override suspend fun unliked(id: Long) {
        try {
            dao.likeById(id)
            val response = PostsApi.retrofitService.unlikedByIdAsync(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = PostsApi.retrofitService.save(post)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: ApiException) {
            throw  e
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun removeById(id: Long) {
        try {
            dao.removeById(id)
            val response = PostsApi.retrofitService.removeById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
        } catch (e: ApiException) {
            throw e
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun getAll() {
        try {
            val response = PostsApi.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: ApiException) {
            throw  e
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }
}



