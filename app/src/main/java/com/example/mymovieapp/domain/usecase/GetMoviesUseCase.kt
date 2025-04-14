package com.example.mymovieapp.domain.usecase

import androidx.paging.PagingData
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<PagingData<Movie>> = repository.getMoviesStream()
}
