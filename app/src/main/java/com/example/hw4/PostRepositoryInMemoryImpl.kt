package com.example.hw4

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likedByMe = false,
        likCount = 5450,
        shareByMe = false,
        shareCount = 1250,
        countView = 4450
    )
    private val data = MutableLiveData(post)
    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(
            likedByMe = (!post.likedByMe),
            likCount = post.likCount + if (post.likedByMe) -1 else 1
        )
        data.value = post

    }

  //  override fun sharing() {
  //      post = post.copy(
  //          shareByMe = (!post.shareByMe),
  //          shareCount = post.shareCount + if (post.shareByMe) -1 else 1
  //      )
  //      data.value = post
  //  }

    override fun sharing() {
        post = post.copy(shareCount = (post.shareCount + 1))
        data.value = post

}

    override fun looking() {
        post = post.copy(countView = (post.countView + 1))
        data.value = post
    }
}