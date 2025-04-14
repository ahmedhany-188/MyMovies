import com.example.mymovieapp.data.local.MovieDao
import com.example.mymovieapp.data.local.MovieEntity
import com.example.mymovieapp.data.remote.MovieDto
import com.example.mymovieapp.data.remote.MoviesApiService
import com.example.mymovieapp.data.repository.MovieRepositoryImpl
import com.example.mymovieapp.domain.model.Movie
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class MovieRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()
    private val mockApi = mock<MoviesApiService>()
    private val mockDao = mock<MovieDao>()
    private val repository = MovieRepositoryImpl(mockApi, mockDao)

    @Test
    fun getMoviesStream_combinesApiAndDbData() = runTest {
        // Arrange
        val apiMovie = MovieDto(id = 1, title = "API Movie")
        val dbEntity = MovieEntity(id = 1, title = "DB Movie")

        whenever(mockApi.fetchMovies(any())).thenReturn(listOf(apiMovie))
        whenever(mockDao.isFavorite(1)).thenReturn(1)

        // Act
        val result = repository.getMoviesStream().first()

        // Assert
        result.collectLatest { pagingData ->
            val items = pagingData.toList()
            assertEquals(1, items.size)
            assertEquals(true, items[0].isFavorite)
        }
    }

    @Test
    fun toggleFavorite_updatesDatabase() = runTest {
        // Arrange
        val testMovie = createTestMovie()
        whenever(mockDao.isFavorite(1)).thenReturn(0)

        // Act
        repository.toggleFavorite(testMovie)

        // Assert
        verify(mockDao).insertFavorite(any())
    }

    private fun createTestMovie() = Movie(
        id = 1,
        title = "Test Movie",
        posterUrl = "https://test.com/poster.jpg",
        releaseDate = "2023",
        overview = "Test overview"
    )
}
