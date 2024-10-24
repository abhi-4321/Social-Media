package com.example.socialmedia.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.socialmedia.model.Comment

@Entity(tableName = "posts_table")
data class PostEntity(
    @PrimaryKey(autoGenerate = false)
    val postId: Int
)
