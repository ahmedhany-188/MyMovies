package com.example.mymovieapp.data.mapper

import com.example.mymovieapp.data.local.MovieEntity
import com.example.mymovieapp.data.remote.MovieDto
import com.example.mymovieapp.domain.model.Movie

fun MovieDto.toDomain() = Movie(
    id = id,
    title = title,
    // Adjust the poster URL if needed:
    posterUrl = poster,
    releaseDate = year.toString(), // or format as desired
    overview = plot,
    genres = genre,
    runtime = runtime,
    isFavorite = false
)

fun Movie.toEntity() = MovieEntity(
    id = id,
    title = title,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    overview = overview
)

fun MovieEntity.toDomain() = Movie(
    id = id,
    title = title,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    overview = overview,
    isFavorite = true
)
