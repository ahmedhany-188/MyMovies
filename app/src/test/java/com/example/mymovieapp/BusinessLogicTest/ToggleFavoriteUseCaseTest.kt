package com.example.mymovieapp.BusinessLogicTest

import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.usecase.ToggleFavoriteUseCase
import com.example.mymovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ToggleFavoriteUseCaseTest {

    private val mockRepo: MovieRepository = mock()
    private val useCase = ToggleFavoriteUseCase(mockRepo)

    @Test
    fun `toggleFavorite should call repository`() = runTest {
        // Given
        val testMovie = createTestMovie()

        // When
        useCase(testMovie)

        // Then
        verify(mockRepo).toggleFavorite(testMovie)
    }

    private fun createTestMovie() = Movie(
        id = 1,
        title = "Test Movie",
        posterUrl = "",
        releaseDate = "2023",
        overview = "Test overview"
    )
}