package com.example.hw4.repository



import com.example.hw4.DTO.Post
import com.example.hw4.api.PostsApi
import com.example.hw4.dao.PostDao
import com.example.hw4.entity.PostEntity
import com.example.hw4.entity.toDto
import com.example.hw4.entity.toEntity
import com.example.hw4.error.ApiException
import com.example.hw4.error.AppError
import com.example.hw4.error.NetworkException
import com.example.hw4.error.UnknownException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.Dispatcher
import java.io.IOException


class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll()
        .map { it.toDto() }
        .flowOn(Dispatchers.Default)


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

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true){
            delay(10000L)
            val response = PostsApi.retrofitService.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insert(body.toEntity().map {
                it.copy(hidden = true)
            })
            emit(body.size)
        }
    }
        .catch { e -> throw  AppError.from(e) }
        .flowOn(Dispatchers.Default)



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



