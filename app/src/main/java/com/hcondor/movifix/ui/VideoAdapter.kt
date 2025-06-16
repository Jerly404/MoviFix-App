package com.hcondor.movifix.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hcondor.movifix.R
import com.hcondor.movifix.model.Movie

class VideoAdapter(
    private val movies: List<Movie>,
    private val onMoreInfoClick: (Movie) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)
        val title: TextView     = itemView.findViewById(R.id.tvTitle)
        val btnMoreInfo: Button  = itemView.findViewById(R.id.btnMoreInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        Glide.with(holder.itemView.context)
            .load(movie.imageUrl)
            .into(holder.thumbnail)

        holder.btnMoreInfo.setOnClickListener {
            onMoreInfoClick(movie)
        }
    }

    override fun getItemCount() = movies.size
}
