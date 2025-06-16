package com.hcondor.movifix.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hcondor.movifix.R
import com.hcondor.movifix.VideoPlayerActivity
import com.hcondor.movifix.model.Movie

class ApiMovieAdapter(
    private val movies: List<Movie>
) : RecyclerView.Adapter<ApiMovieAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.imgThumbnail)
        val title: TextView = v.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val m = movies[pos]
        holder.title.text = m.title

        Glide.with(holder.itemView.context)
            .load(m.imageUrl)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            Log.d("ApiMovieAdapter", "Clic en: ${m.title}, videoUrl: ${m.videoUrl}")

            if (!m.videoUrl.isNullOrBlank()) {
                val context = holder.itemView.context
                val intent = Intent(context, VideoPlayerActivity::class.java).apply {
                    putExtra("videoUrl", m.videoUrl)
                    putExtra("title", m.title)
                    putExtra("description", m.description ?: "Sin descripción")
                }
                context.startActivity(intent)
            } else {
                Toast.makeText(holder.itemView.context, "Video no disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = movies.size
}
