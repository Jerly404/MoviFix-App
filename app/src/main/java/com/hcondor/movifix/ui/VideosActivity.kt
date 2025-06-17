package com.hcondor.movifix.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hcondor.movifix.R
import com.hcondor.movifix.VideoPlayerActivity
import com.hcondor.movifix.database.VideoDatabaseHelper
import com.hcondor.movifix.model.Movie

class VideosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: VideoAdapter
    private lateinit var dbHelper: VideoDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)

        recyclerView = findViewById(R.id.rvVideoMovies)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        dbHelper = VideoDatabaseHelper(this)

        val bottomNavigationView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_video

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_video -> true
                R.id.nav_favorite -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Inserta películas si la base está vacía
        if (dbHelper.getAllMovies().isEmpty()) {
            val initialMovies = listOf(
                Movie(
                    title = "Inception",
                    year = "2010",
                    description = "Un ladrón roba secretos corporativos mediante tecnología de sueños compartidos.",
                    authors = "Christopher Nolan",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                    imageUrl = "https://i.pinimg.com/736x/b0/ae/a4/b0aea49646879a043ad9f6ec3002e99f.jpg"
                ),
                Movie(
                    title = "Interstellar",
                    year = "2014",
                    description = "Un grupo de astronautas viaja por un agujero de gusano para salvar la humanidad.",
                    authors = "Christopher Nolan",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg"
                ),
                Movie(
                    title = "The Dark Knight",
                    year = "2008",
                    description = "Batman lucha contra el Joker, un criminal que siembra el caos en Gotham.",
                    authors = "Christopher Nolan",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg"
                ),
                Movie(
                    title = "The Matrix",
                    year = "1999",
                    description = "Un hacker descubre que la realidad es una simulación controlada por máquinas.",
                    authors = "Lana Wachowski, Lilly Wachowski",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg"
                ),
                Movie(
                    title = "Avengers: Endgame",
                    year = "2019",
                    description = "Los Vengadores se reúnen para derrotar a Thanos y restaurar el universo.",
                    authors = "Anthony Russo, Joe Russo",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/ulzhLuWrPK07P1YkdWQLZnQh1JL.jpg"
                ),
                Movie(
                    title = "Joker",
                    year = "2019",
                    description = "La historia del origen de Joker, el icónico villano de Batman.",
                    authors = "Todd Phillips",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg"
                ),
                Movie(
                    title = "Parasite",
                    year = "2019",
                    description = "Una familia pobre se infiltra en la vida de una familia rica, con consecuencias inesperadas.",
                    authors = "Bong Joon-ho",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg"
                ),
                Movie(
                    title = "Spider-Man: No Way Home",
                    year = "2021",
                    description = "Peter Parker pide ayuda a Doctor Strange, pero algo sale mal.",
                    authors = "Jon Watts",
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg"
                )
            )
            for (movie in initialMovies) {
                dbHelper.insertMovie(movie)
            }
        }

        val movies = dbHelper.getAllMovies()
        movieAdapter = VideoAdapter(
            movies,
            onMoreInfoClick = { movie ->
                val intent = Intent(this, MovieDetailActivity::class.java).apply {
                    putExtra("title", movie.title)
                    putExtra("year", movie.year)
                    putExtra("description", movie.description)
                    putExtra("authors", movie.authors)
                    putExtra("videoUrl", movie.videoUrl)
                    putExtra("imageUrl", movie.imageUrl)
                }
                startActivity(intent)
            },
            onFavoriteClick = { movie ->
                dbHelper.toggleFavorite(movie)
                movieAdapter.notifyDataSetChanged()
                Toast.makeText(this, "${movie.title} favorito actualizado", Toast.LENGTH_SHORT).show()
            },
            isFavoriteChecker = { movie ->
                dbHelper.isMovieFavorite(movie.title)
            },
            onVideoClick = { movie -> // ✅ nuevo callback para abrir reproductor
                val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                    putExtra("videoUrl", movie.videoUrl)
                }
                startActivity(intent)
            }
        )

        recyclerView.adapter = movieAdapter
    }
}
