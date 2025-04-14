package com.example.mymovieapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.usecase.GetMoviesUseCase
import com.example.mymovieapp.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getMoviesUseCase: GetMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val movies = getMoviesUseCase().cachedIn(viewModelScope)

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            toggleFavoriteUseCase(movie)
            // Refresh the list after toggle
//            movies.value?.refresh()
        }
    }

}
