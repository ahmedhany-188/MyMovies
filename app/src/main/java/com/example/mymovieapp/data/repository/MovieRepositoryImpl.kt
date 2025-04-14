package com.example.mymovieapp.data.repository

import androidx.paging.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mymovieapp.data.local.MovieDao
import com.example.mymovieapp.data.mapper.toDomain
import com.example.mymovieapp.data.mapper.toEntity
import com.example.mymovieapp.data.paging.MoviesPagingSource
import com.example.mymovieapp.data.remote.MoviesApiService
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val service: MoviesApiService,
    private val dao: MovieDao
) : MovieRepository {
    override fun getMoviesStream(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviesPagingSource(service) }
        ).flow.map { pagingData ->
            pagingData.map { movieDto ->
//                val movie = movieDto.toDomain()
                movieDto.copy(
                    isFavorite = dao.isFavorite(movieDto.id) == 1
                )
            }
        }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return dao.getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun toggleFavorite(movie: Movie) {
        withContext(Dispatchers.IO) {
            if (dao.isFavorite(movie.id) == 1) {
                dao.deleteFavorite(movie.toEntity())
            } else {
                dao.insertFavorite(movie.toEntity())
            }
        }
    }

    override suspend fun getMovieDetail(movieId: Int): Movie {
        val response = service.fetchMovies(1)
        return response.first { it.id == movieId }.toDomain()
    }
}
