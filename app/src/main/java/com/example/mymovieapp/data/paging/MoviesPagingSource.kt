package com.example.mymovieapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mymovieapp.data.remote.MoviesApiService
import com.example.mymovieapp.data.mapper.toDomain
import com.example.mymovieapp.domain.model.Movie

class MoviesPagingSource(
    private val service: MoviesApiService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1

        return try {
            // Call the API with page and limit
            val response = service.fetchMovies(page, params.loadSize)
            val movies = response.map { it.toDomain() }
            val nextPage = if (movies.isEmpty()) null else page + 1

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPos)?.nextKey?.minus(1)
        }
    }
}
