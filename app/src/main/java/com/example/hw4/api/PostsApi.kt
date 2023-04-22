package com.example.hw4.api

import com.example.hw4.BuildConfig
import com.example.hw4.DTO.Media
import com.example.hw4.DTO.Post
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*

private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"

interface PostsApiService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{postId}/newer")
    suspend fun getNewer(@Path("postId") id: Long): Response<List<Post>>

    @POST("posts/{postId}/likes")
    suspend fun likedById(@Path("postId") id: Long): Response<Post>

    @DELETE("posts/{postId}/likes")
    suspend fun unlikedByIdAsync(@Path("postId") id: Long): Response<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @DELETE("posts/{postId}")
    suspend fun removeById(@Path("postId") id: Long): Response<Unit>
    @Multipart
    @POST("media")
    suspend fun upload(@Part file: MultipartBody.Part): Response<Media>

}

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.HEADERS
    }
}
private val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .baseUrl(BASE_URL)
    .build()

object PostsApi {
    val service: PostsApiService by lazy {
        retrofit.create(PostsApiService::class.java)
    }
}