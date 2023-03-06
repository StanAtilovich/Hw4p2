package com.example.hw4.repository

import androidx.lifecycle.Transformations
import com.example.hw4.DTO.Post
import com.example.hw4.dao.PostDao
import com.example.hw4.entity.PostEntity

class PostRepositoryImpl (
    private val dao: PostDao,
) : PostRepository {
    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            it.toDto()
        }
    }


    override fun sharing(id: Long) {
        dao.sharing(id)
    }

    override fun likedById(id: Long) {
        dao.likeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}