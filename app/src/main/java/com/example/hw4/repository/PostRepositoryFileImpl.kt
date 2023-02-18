package com.example.hw4.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hw4.DTO.Post
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PostRepositoryFileImpl(
    private val context: Context,
):PostRepository {
    private val gson = Gson()//Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "post.json"
    private var nextId = 1L
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()){
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, type)
                data.value = posts
            }

        }else{
            sync()
        }
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    override fun get(): LiveData<List<Post>> = data


    override fun likedById(id: Long) {
        posts = posts.map {
            if (it.id == id) {
                it.copy(
                    likedByMe = !it.likedByMe, likCount = if (it.likedByMe) {
                        it.likCount - 1
                    } else {
                        it.likCount + 1
                    }
                )
            } else {
                it
            }
        }
        data.value = posts
        sync()
    }

    override fun sharing(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shareCount = it.shareCount + 1)
        }

        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = ++nextId,
                    author = "me",
                    likedByMe = false,
                    published = "now",
                )
            ) + posts
            data.value = posts
            return
        }
        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
        sync()
    }
}