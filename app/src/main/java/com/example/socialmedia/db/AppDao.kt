package com.example.socialmedia.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.socialmedia.db.entities.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Upsert
    suspend fun upsertPost(post: PostEntity)

    @Query("""
         Select count(*) from posts_table
    """)
    suspend fun getCount() : Int

    @Query(""" 
        Select * from posts_table
    """)
    fun getAllPosts() : Flow<List<PostEntity>>

    @Query("""
        Select * from posts_table
        where id = :id
    """)
    suspend fun getPostById(id: Int) : PostEntity

}