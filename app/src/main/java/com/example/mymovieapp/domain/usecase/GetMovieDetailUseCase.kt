package com.example.mymovieapp.domain.usecase

import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Movie {
        return repository.getMovieDetail(movieId)
    }
}
