package com.example.hw4.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hw4.DTO.Post
import com.example.hw4.model.FeedModel
import com.example.hw4.repository.PostRepository
import com.example.hw4.repository.PostRepositoryImpl
import com.example.hw4.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    likCount = 0,
    shareByMe = false,
    shareCount = 0,
    viewByMe = false,
    countView = 0,
    video = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun refreshPosts() {
        thread {
            _data.postValue(FeedModel(refreshing = true))
            try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }

    private fun reload(isInitial: Boolean) {
        thread {
            _data.postValue(FeedModel(loading = isInitial, refreshing = !isInitial))
            try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }

    fun loadPosts() {
        reload(true)
    }


    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun clear() {
        edited.value = empty
    }


    fun sharing(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.run {
                postValue(
                    value?.copy(
                        posts = value?.posts.orEmpty().map {
                            if (it.id != id) it else it.copy(shareCount = it.shareCount + 1)
                        }
                    ))
                try {
                    repository.sharing(id)
                } catch (e: IOException) {
                    _data.postValue(_data.value?.copy(posts = old))
                }
            }
        }
    }

    fun likeById(id: Long) {
        thread {
            val post = repository.likedById(id)
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty().map {
                if (it.id == id) {
                    post
                } else {
                    it
                }
            }))
        }
    }

    fun unlikeById(id: Long) {
        thread {
            val post=repository.unlikedById(id)
            _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().map {
                if (it.id == id) {
                    post
                } else {
                    it
                }
            }))}
    }
    //   fun likeById(id: Long) {
    //       thread {
    //           val old = _data.value?.posts.orEmpty()
    //           _data.postValue(
    //               _data.value?.copy(posts = _data.value?.posts.orEmpty().map {
    //                   if (it.id == id) {
    //                       it.copy(
    //                           likedByMe = !it.likedByMe, likCount = if (it.likedByMe) {
    //                               it.likCount - 1
    //                           } else {
    //                               it.likCount + 1
    //                           }
    //                       )
    //                   } else {
    //                       it
    //                   }
    //               }
    //               )
    //           )
    //           try {
    //               repository.likedById(id)
    //           } catch (e: IOException) {
    //               _data.postValue(_data.value?.copy(posts = old))
    //           }
    //       }
    //   }

    fun removeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }
}