package com.example.mymovieapp.ViewModelTest

import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.usecase.GetMoviesUseCase
import com.example.mymovieapp.domain.usecase.ToggleFavoriteUseCase
import com.example.mymovieapp.ui.home.HomeViewModel

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    // 2. Mock Dependencies
    private val mockGetMoviesUseCase = mock<GetMoviesUseCase>()
    private val mockToggleUseCase = mock<ToggleFavoriteUseCase>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 3. Test Movie Creation Helper
    private fun createTestMovie() = Movie(
        id = 1,
        title = "Test Movie",
        posterUrl = "https://example.com/poster.jpg",
        releaseDate = "2023-01-01",
        overview = "Test overview",
        isFavorite = false
    )

    @Test
    fun `movies should emit paging data`() = testScope.runTest {
        // Given
        val testMovie = createTestMovie()
        val pagingData = PagingData.from(listOf(testMovie))

        whenever(mockGetMoviesUseCase.invoke()).thenReturn(flowOf(pagingData))

        // When
        val viewModel = HomeViewModel(mockGetMoviesUseCase, mockToggleUseCase)

        // Then
        viewModel.movies.test {  // Using Turbine for Flow testing
            val result = awaitItem()
            val items = mutableListOf<Movie>()
//            result.collect { pagingData ->
//                pagingData.map { items.add(it) }
//            }
            assertEquals(1, items.size)  // Fixes error 3
            assertEquals("Test Movie", items[0].title)
            cancel()
        }
    }

    @Test
    fun `toggleFavorite should trigger refresh`() = testScope.runTest {
        // Given
        val viewModel = HomeViewModel(mockGetMoviesUseCase, mockToggleUseCase)
        val testMovie = createTestMovie()

        // When
        viewModel.toggleFavorite(testMovie)
        testScheduler.advanceUntilIdle()

        // Then
        verify(mockGetMoviesUseCase, times(2)).invoke()
    }
}