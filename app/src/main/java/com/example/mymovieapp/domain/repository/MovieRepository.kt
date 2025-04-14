package com.example.mymovieapp.domain.repository

import androidx.paging.PagingData
import com.example.mymovieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMoviesStream(): Flow<PagingData<Movie>>
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun toggleFavorite(movie: Movie)
    suspend fun getMovieDetail(movieId: Int): Movie
}
