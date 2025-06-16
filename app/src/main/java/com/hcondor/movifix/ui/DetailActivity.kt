package com.hcondor.movifix.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.hcondor.movifix.R

// Nueva clase DetailActivity.kt
class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("title")
        val year = intent.getStringExtra("year")
        val poster = intent.getStringExtra("poster")

        // Aquí haces findViewById para tus vistas y asignas los valores
        // Por ejemplo:
        val tvTitle = findViewById<TextView>(R.id.tvTitleDetail)
        val tvYear = findViewById<TextView>(R.id.tvYearDetail)
        val ivPoster = findViewById<ImageView>(R.id.ivPosterDetail)

        tvTitle.text = title
        tvYear.text = year

        Glide.with(this)
            .load(poster)
            .placeholder(R.drawable.ic_placeholder)
            .into(ivPoster)
    }
}
