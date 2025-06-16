package com.hcondor.movifix.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hcondor.movifix.database.VideoDatabaseHelper
import com.hcondor.movifix.databinding.ActivityVideosBinding
import com.hcondor.movifix.model.Video
import com.hcondor.movifix.R
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

        if (dbHelper.getAllMovies().isEmpty()) {
            val initialMovies = listOf(
                Movie(
                    title = "Inception",
                    year = "2010",
                    description = "Un ladrón roba secretos corporativos mediante tecnología de sueños compartidos.",
                    authors = "Christopher Nolan",
                    videoUrl = "https://path.to/inception.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg"
                ),
                Movie(
                    title = "Interstellar",
                    year = "2014",
                    description = "Un grupo de astronautas viaja por un agujero de gusano para salvar la humanidad.",
                    authors = "Christopher Nolan",
                    videoUrl = "https://path.to/interstellar.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg"
                ),
                Movie(
                    title = "The Dark Knight",
                    year = "2008",
                    description = "Batman lucha contra el Joker, un criminal que siembra el caos en Gotham.",
                    authors = "Christopher Nolan",
                    videoUrl = "https://path.to/darkknight.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg"
                ),
                Movie(
                    title = "The Matrix",
                    year = "1999",
                    description = "Un hacker descubre que la realidad es una simulación controlada por máquinas.",
                    authors = "Lana Wachowski, Lilly Wachowski",
                    videoUrl = "https://path.to/matrix.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg"
                ),
                Movie(
                    title = "Avengers: Endgame",
                    year = "2019",
                    description = "Los Vengadores se reúnen para derrotar a Thanos y restaurar el universo.",
                    authors = "Anthony Russo, Joe Russo",
                    videoUrl = "https://path.to/endgame.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/ulzhLuWrPK07P1YkdWQLZnQh1JL.jpg"
                ),
                Movie(
                    title = "Spider-Man: No Way Home",
                    year = "2021",
                    description = "Peter Parker enfrenta enemigos de otros universos tras un hechizo fallido.",
                    authors = "Jon Watts",
                    videoUrl = "https://path.to/spiderman.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg"
                ),
                Movie(
                    title = "Joker",
                    year = "2019",
                    description = "La transformación de Arthur Fleck en el icónico villano Joker.",
                    authors = "Todd Phillips",
                    videoUrl = "https://path.to/joker.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg"
                ),
                Movie(
                    title = "Fight Club",
                    year = "1999",
                    description = "Un hombre forma un club secreto de peleas con un misterioso vendedor de jabón.",
                    authors = "David Fincher",
                    videoUrl = "https://path.to/fightclub.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/bptfVGEQuv6vDTIMVCHjJ9Dz8PX.jpg"
                ),
                Movie(
                    title = "Pulp Fiction",
                    year = "1994",
                    description = "Historias entrelazadas de crimen, redención y violencia en Los Ángeles.",
                    authors = "Quentin Tarantino",
                    videoUrl = "https://path.to/pulpfiction.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/dM2w364MScsjFf8pfMbaWUcWrR.jpg"
                ),
                Movie(
                    title = "Forrest Gump",
                    year = "1994",
                    description = "Un hombre con un bajo coeficiente intelectual presencia eventos históricos.",
                    authors = "Robert Zemeckis",
                    videoUrl = "https://path.to/forrestgump.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/saHP97rTPS5eLmrLQEcANmKrsFl.jpg"
                ),
                Movie(
                    title = "Gladiator",
                    year = "2000",
                    description = "Un general romano busca venganza como gladiador tras ser traicionado.",
                    authors = "Ridley Scott",
                    videoUrl = "https://path.to/gladiator.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/ty8TGRuvJLPUmAR1H1nRIsgwvim.jpg"
                ),
                Movie(
                    title = "The Shawshank Redemption",
                    year = "1994",
                    description = "Un banquero es encarcelado por un crimen que no cometió y planea su escape.",
                    authors = "Frank Darabont",
                    videoUrl = "https://path.to/shawshank.mp4",
                    imageUrl = "https://image.tmdb.org/t/p/w500/5cIUvCJQ2aNPXRCmXiOIuJJxIki.jpg"
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