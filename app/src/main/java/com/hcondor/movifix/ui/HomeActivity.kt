package com.hcondor.movifix.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hcondor.movifix.R
import com.hcondor.movifix.model.Movie
import com.hcondor.movifix.model.MovieResponse
import com.hcondor.movifix.network.MovieService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private val apiKey = "d9ff1c9c"
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: ApiMovieAdapter
    private val moviesList = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.rvMovies)
        recyclerView.layoutManager = LinearLayoutManager(this)
        movieAdapter = ApiMovieAdapter(moviesList)
        recyclerView.adapter = movieAdapter

        findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> true
                    R.id.nav_video -> {
                        startActivity(Intent(this, VideosActivity::class.java))
                        true
                    }
                    R.id.nav_favorite -> { /*…*/ true }
                    R.id.nav_profile  -> { /*…*/ true }
                    else -> false
                }
            }

        Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieService::class.java)
            .searchMovies(apiKey, "Avengers")
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(c: Call<MovieResponse>, r: Response<MovieResponse>) {
                    if (r.isSuccessful) {
                        // dentro de onResponse(...)
                        r.body()?.search?.let { apiList ->
                            moviesList.clear()
                            moviesList.addAll(apiList) // cada apiMovie ya tiene title/year/imageUrl correctos
                            movieAdapter.notifyDataSetChanged()
                        }

                    }
                }
                override fun onFailure(c: Call<MovieResponse>, t: Throwable) {
                    Log.e("API", "Error: ${t.message}")
                }
            })
    }
}