package com.example.hw4.DTO

import android.net.Uri
import com.example.hw4.entity.Attachment
import java.io.File


data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    val likes: Int,
    val shareByMe: Boolean = false,
    val shareCount: Int,
    val viewByMe: Boolean = false,
    val countView: Int,
    val video: String?,
    val authorAvatar: String,
    val attachment: Attachment? = null,
    val hidden: Boolean,
    )
data class PhotoModel(val uri: Uri?, val file: File?)
data class Media(val id: String)






