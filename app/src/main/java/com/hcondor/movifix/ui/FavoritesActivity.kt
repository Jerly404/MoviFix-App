package com.hcondor.movifix.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.hcondor.movifix.VideoPlayerActivity
import com.hcondor.movifix.database.VideoDatabaseHelper
import com.hcondor.movifix.databinding.ActivityFavoritesBinding
import com.hcondor.movifix.model.Movie

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var dbHelper: VideoDatabaseHelper
    private lateinit var movieAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = VideoDatabaseHelper(this)
        loadFavorites()
    }

    private fun loadFavorites() {
        val favorites = dbHelper.getAllFavorites()

        movieAdapter = VideoAdapter(
            favorites,
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
                dbHelper.removeFavorite(movie)
                loadFavorites() // Recargar la lista después de eliminar
            },
            isFavoriteChecker = { true }, // Todos son favoritos en esta vista
            onVideoClick = { movie -> // ✅ Corrección añadida
                val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                    putExtra("videoUrl", movie.videoUrl)
                }
                startActivity(intent)
            }
        )

        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(this@FavoritesActivity, 2)
            adapter = movieAdapter
        }
    }
}
