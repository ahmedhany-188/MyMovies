package com.example.mymovieapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {
    @GET("movies")
    suspend fun fetchMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20   // Use limit to control page size.
    ): List<MovieDto>

    // For single record:
//    @GET("movies/{id}")
//    suspend fun getMovieDetail(
//        @retrofit2.http.Path("id") movieId: Int
//    ): MovieDto
}
