package com.hcondor.movifix.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.hcondor.movifix.model.MovieResponse
import com.hcondor.movifix.network.MovieService

class MovieRepository(private val movieService: MovieService) {

    fun getPopularMovies(
        apiKey: String,
        onSuccess: (MovieResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = movieService.getPopularMovies(apiKey)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                        ?: onError(Throwable("Response body is null"))
                } else {
                    onError(Throwable("Response not successful: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                onError(t)
            }
        })
    }
}
