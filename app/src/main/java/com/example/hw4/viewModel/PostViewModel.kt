package com.example.hw4.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.example.hw4.DTO.PhotoModel
import com.example.hw4.DTO.Post
import com.example.hw4.db.AppDb
import com.example.hw4.model.FeedModel
import com.example.hw4.model.FeedModelState
import com.example.hw4.repository.PostRepository
import com.example.hw4.repository.PostRepositoryImpl
import com.example.hw4.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File


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
    attachment = null,
    hidden = false
)
private val noPhoto = PhotoModel(null, null)

class PostViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(application).postDao())
    private val _dataState = MutableLiveData(FeedModelState())
    val data: LiveData<FeedModel> = repository.data
        .map { FeedModel(it, it.isEmpty()) }
        .asLiveData(Dispatchers.Default)

    val newerCount: LiveData<Int> = data.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0)
            .catch { e -> _dataState.postValue(FeedModelState(error = true)) }
            .asLiveData(Dispatchers.Default)
    }

    val dataState: LiveData<FeedModelState>
        get() = _dataState

    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo


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


    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
            _dataState.value = FeedModelState(Shadow = true)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun likeById(id: Long) = viewModelScope.launch {
        try {
            repository.likedById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            repository.removeById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }


    fun save() {
        edited.value?.let {
            _postCreated.postValue(Unit)
            viewModelScope.launch {
                try {
                    when(_photo.value){
                        noPhoto -> repository.save(it)
                        else -> _photo.value?.file?.let {
                            file -> repository.saveWithAttachment(it, file)
                        }
                    }
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
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


    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, null)
    }

    fun deletePhoto() {
        _photo.value = noPhoto
    }

    fun newPostView() = viewModelScope.launch {
        try {
            repository.getVisible()
            _dataState.value = FeedModelState(Shadow = true)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun readAll() = viewModelScope.launch {
        try {
            repository.readAll()
            _dataState.value = FeedModelState(Shadow = true)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }


}


