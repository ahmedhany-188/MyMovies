package com.example.mymovieapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterUrl: String,
    val releaseDate: String,
    val overview: String?
)
