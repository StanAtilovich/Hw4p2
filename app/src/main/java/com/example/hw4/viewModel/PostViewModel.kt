package com.example.hw4.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.hw4.DTO.Post
import com.example.hw4.db.AppDb
import com.example.hw4.model.FeedModel
import com.example.hw4.model.FeedModelState
import com.example.hw4.repository.PostRepository
import com.example.hw4.repository.PostRepositoryImpl
import com.example.hw4.util.SingleLiveEvent
import kotlinx.coroutines.launch


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    likes = 0,
    shareByMe = false,
    shareCount = 0,
    viewByMe = false,
    countView = 0,
    video = null,
    authorAvatar = "",
    attachment = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(AppDb.getInstance(application).postDao())
    private val _dataState = MutableLiveData(FeedModelState())
    val data: LiveData<FeedModel> = repository.data.map { FeedModel(it, it.isEmpty()) }
    val dataState: LiveData<FeedModelState>
        get() = _dataState
    val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error

    init {
        loadPosts()
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
   }



    fun reload() = viewModelScope.launch {
    try {
        _dataState.value = FeedModelState(loading = true)
        repository.getAll()
            _dataState.value =FeedModelState()
        }catch (e: Exception){
        _dataState.value = FeedModelState(error = true)}
    }



    fun loadPosts() {
        reload()
    }


    fun save() {
   //    edited.value?.let {
   //        repository.save(it, object : PostRepository.SaveRemoveCallback {
   //            override fun onSuccess() {
   //                _postCreated.postValue(Unit)
   //            }
   //        })
   //    }
   //    edited.value = empty
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



    //     fun likeById(id: Long, likedByMe: Boolean) {
    //         repository.likedByIdAsync(id, likedByMe, object : PostRepository.Callback<Post> {
    //             override fun onSuccess(posts: Post) {
    //                 _postCreated.postValue(Unit)
    //                 _data.value?.copy(posts = _data.value?.posts.orEmpty()
    //                     .map {
    //                         if (it.id != id) {
    //                             it
    //                         } else {
    //                             posts
    //                         }
    //                     }
    //                 ) }
    //             override fun onError(e: Exception) {
    //                 _data.postValue(FeedModel(error = true))
    //             }
    //         })
    //     }
//

    fun likeById(id: Long, likedByMe: Boolean) {
    //  repository.likedByIdAsync(id, likedByMe, object : PostRepository.Callback<Post> {
    //      override fun onSuccess(posts: Post) {
    //          _postCreated.postValue(Unit)
    //          _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty()
    //              .map {
    //                  if (it.id != id) {
    //                      it
    //                  } else {
    //                      posts
    //                  }
    //              }
    //          ))
            }

      //      override fun onError(e: Exception) {
      //          _data.postValue(FeedModel(error = true))
      //      }
       // })
  //  }

    fun removeById(id: Long) {
    //    val old = _dataState.value?.posts.orEmpty()
    //    _dataState.postValue(
    //        _dataState.value?.copy(posts = _dataState.value?.posts.orEmpty()
    //            .filter { it.id != id }
    //        )
      //  )
     //   repository.removeById(id, object : PostRepository.SaveRemoveCallback {
     //       override fun onError(e: Exception) {
     //           _data.postValue(_data.value?.copy(posts = old))
     //       }
     //   })
    }


}