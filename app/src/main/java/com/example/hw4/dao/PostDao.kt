package com.example.hw4.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hw4.entity.PostEntity


@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
   fun getAll(): LiveData<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend  fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend  fun insert(posts: List<PostEntity>)


    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Query("""
        UPDATE PostEntity SET
        likCount = likCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
    """)
    suspend fun likeById(id: Long)


}