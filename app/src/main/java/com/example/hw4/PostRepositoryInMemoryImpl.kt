package com.example.hw4

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 0L
    private var post = listOf(

        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Номер $nextId Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likCount = 5450,
            shareByMe = false,
            shareCount = 1250,
            // viewByMe = false,
            countView = 4450
        )
    ).reversed()
    private val data = MutableLiveData(post)
    override fun get(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        this.post = listOf(
            post.copy(
                id = nextId + 1,
                author = "me",
                likedByMe = false,
                published = "now",
            )
        ) + post
        data.value = listOf(post)
        return



    }





    override fun removeById(id: Long) {
        post = post.filter { it.id != id }
        data.value = post
    }




    override fun likedById(id: Long) {
        post = post.map {
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
        data.value = post

    }

    override fun sharing(id: Long) {
        post = post.map {
            if (it.id == id) {
                it.copy(
                    shareByMe = !it.shareByMe, shareCount = if (it.shareByMe) {
                        it.shareCount - 1
                    } else {
                        it.shareCount + 1
                    }
                )
            } else {
                it
            }
        }
        data.value = post
    }

    override fun looking() {
        TODO("Not yet implemented")
    }


    //  override fun looking(id: Long) {
    //      post = post.map {
    //          if (it.id == id) {
    //              it.copy(countView = (post.countView + 1))
    //              data.value = post
    //          }
}


// post = post.map {  }(countView = (post.countView + 1))
//  data.value = post


//}

//  override fun sharing() {
//      post = post.copy(
//          shareByMe = (!post.shareByMe),
//          shareCount = post.shareCount + if (post.shareByMe) -1 else 1
//      )
//      data.value = post
//  }

//    override fun sharing() {
//        post = post.copy(shareCount = (post.shareCount + 1))
//        data.value = post

//    }

//    override fun looking() {
//        post = post.copy(countView = (post.countView + 1))
//    }
//}