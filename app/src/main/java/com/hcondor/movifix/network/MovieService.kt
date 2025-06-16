package com.hcondor.movifix.network

import com.hcondor.movifix.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("/")
    fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String
    ): Call<MovieResponse>

    @GET("/")
    fun getPopularMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String = "popular"
    ): Call<MovieResponse>
}
