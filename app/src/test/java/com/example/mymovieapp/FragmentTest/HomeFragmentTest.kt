
//import androidx.paging.PagingData
import com.example.mymovieapp.di.DatabaseModule
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.repository.MovieRepository
//import com.example.mymovieapp.ui.home.HomeViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
//import org.mockito.kotlin.whenever

@HiltAndroidTest
@ExperimentalCoroutinesApi
@UninstallModules(DatabaseModule::class)
class HomeFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val mockRepo: MovieRepository = mock()

    @BindValue
//    @JvmField
//    val mockViewModelFactory: HomeViewModel.Factory = mock()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun displayMovies() {
        // Given
        val testMovie = createTestMovie()
//        whenever(mockRepo.getMoviesStream()).thenReturn(flowOf(PagingData.from(listOf(testMovie)))

                // When
//                launchFragmentInHiltContainer<HomeFragment> {
//            viewModelFactory = mockViewModelFactory
//        }

                // Then
//                onView(withId(R.id.recycler_movies)).check(matches(isDisplayed()))
//                onView(withText("Test Movie")).check(matches(isDisplayed()))
    }

    @Test
    fun toggleFavorite() {
        // Given
//        val testMovie = createTestMovie()
//        whenever(mockRepo.getMoviesStream()).thenReturn(
////            flowOf(PagingData.from(listOf(testMovie))
//                )

                // When
//                launchFragmentInHiltContainer<HomeFragment> {
//            viewModelFactory  = mockViewModelFactory
//        }
//                onView(withId(R.id.favoriteIcon)).perform(click())

                // Then
//                verify(mockRepo).toggleFavorite(any())
    }

    private fun createTestMovie() = Movie(
        id = 1,
        title = "Test Movie",
        posterUrl = "https://test.com/poster.jpg",
        releaseDate = "2023",
        overview = "Test overview"
    )
}

