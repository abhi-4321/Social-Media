package com.example.socialmedia.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.socialmedia.db.entities.PostEntity

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao() : AppDao

}