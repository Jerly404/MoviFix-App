package com.hcondor.movifix.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hcondor.movifix.R

class MovieDetailActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_movie_detail)

        val title = intent.getStringExtra("title")
        val year = intent.getStringExtra("year")
        val description = intent.getStringExtra("description")
        val authors = intent.getStringExtra("authors")
        val videoUrl = intent.getStringExtra("videoUrl")
        val imageUrl = intent.getStringExtra("imageUrl")

        findViewById<TextView>(R.id.tvTitle).text = title
        findViewById<TextView>(R.id.tvYear).text = year
        findViewById<TextView>(R.id.tvDescription).text = description
        findViewById<TextView>(R.id.tvAuthors).text = authors
        Glide.with(this).load(imageUrl).into(findViewById<ImageView>(R.id.ivPoster))

        findViewById<Button>(R.id.btnWatchMovie)
            .setOnClickListener {
                startActivity(Intent(this, VideoPlayerActivity::class.java)
                    .putExtra("videoUrl", videoUrl))
            }
    }
}
