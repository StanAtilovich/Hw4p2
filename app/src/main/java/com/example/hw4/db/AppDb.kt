package com.example.hw4.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hw4.dao.Converters
import com.example.hw4.dao.PostDao
import com.example.hw4.entity.PostEntity


@TypeConverters(Converters::class)
@Database(entities = [PostEntity::class], version = 1,exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
}
