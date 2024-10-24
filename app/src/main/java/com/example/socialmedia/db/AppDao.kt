package com.example.socialmedia.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.socialmedia.db.entities.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

//    @Upsert
//    suspend fun upsert(postId: Int)

    @Query(""" 
        Select * from posts_table
    """)
    fun getAllPostIds() : Flow<List<Int>>

    @Query("""
        Delete from posts_table
        where postId =:postId
    """)
    suspend fun delete(postId: Int)

}