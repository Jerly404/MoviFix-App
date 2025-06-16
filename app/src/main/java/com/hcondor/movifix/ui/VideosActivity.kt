package com.hcondor.movifix.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hcondor.movifix.database.VideoDatabaseHelper
import com.hcondor.movifix.databinding.ActivityVideosBinding
import com.hcondor.movifix.model.Movie
import com.hcondor.movifix.R

class VideosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: VideoAdapter
    private lateinit var dbHelper: VideoDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)

        recyclerView = findViewById(R.id.rvVideoMovies)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        dbHelper = VideoDatabaseHelper(this) // ✅ Primero se inicializa
        dbHelper.deleteAllMovies() // ✅ Luego puedes usarlo

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
                )
            )

            for (movie in initialMovies) {
                dbHelper.insertMovie(movie)
            }
        }

        val movies = dbHelper.getAllMovies()
        movieAdapter = VideoAdapter(movies) { movie ->
            val intent = Intent(this, MovieDetailActivity::class.java).apply {
                putExtra("title", movie.title)
                putExtra("year", movie.year)
                putExtra("description", movie.description)
                putExtra("authors", movie.authors)
                putExtra("videoUrl", movie.videoUrl)
                putExtra("imageUrl", movie.imageUrl)
            }
            startActivity(intent)
        }
        recyclerView.adapter = movieAdapter
    }
}
