package com.example.mymovieapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.domain.usecase.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Updated DetailViewModel.kt
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    sealed class MovieState {
        data object Loading : MovieState()
        data class Success(val movie: com.example.mymovieapp.domain.model.Movie) : MovieState()
        data class Error(val errorType: ErrorType) : MovieState()
    }

    enum class ErrorType {
        NETWORK, SERVER, GENERIC
    }

    private val _movieState = MutableStateFlow<MovieState>(MovieState.Loading)
    val movieState: StateFlow<MovieState> = _movieState.asStateFlow()

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _movieState.value = MovieState.Loading
            try {
                val movie = getMovieDetailUseCase(movieId)
                _movieState.value = MovieState.Success(movie)
            } catch (e: Exception) {
                _movieState.value = MovieState.Error(
                    when (e) {
                        is java.net.UnknownHostException -> ErrorType.NETWORK
                        is retrofit2.HttpException -> ErrorType.SERVER
                        else -> ErrorType.GENERIC
                    }
                )
            }
        }
    }
}