package com.hcondor.movifix.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hcondor.movifix.R
import com.hcondor.movifix.model.Movie

class VideoAdapter(
    private val movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.ivPoster)
        val title: TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val m = movies[position]
        holder.title.text = m.title
        Glide.with(holder.itemView.context)
            .load(m.imageUrl)
            .into(holder.img)
        holder.itemView.setOnClickListener { onMovieClick(m) }
    }

    override fun getItemCount() = movies.size
}
