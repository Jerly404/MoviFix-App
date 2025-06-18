package com.hcondor.movifix.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hcondor.movifix.R
import com.hcondor.movifix.model.Movie

class VideoAdapter(
    private val movies: List<Movie>,
    private val onMoreInfoClick: (Movie) -> Unit,
    private val onFavoriteClick: (Movie) -> Unit,
    private val isFavoriteChecker: (Movie) -> Boolean,
    private val onVideoClick: (Movie) -> Unit
) : RecyclerView.Adapter<VideoAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.imgThumbnail)
        val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
        val btnMoreInfo: ImageButton = itemView.findViewById(R.id.btnMoreInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.imgPoster.setOnClickListener {
            onVideoClick(movie)
        }

        // Cargar imagen del póster
        Glide.with(holder.itemView).load(movie.imageUrl).into(holder.imgPoster)

        // Cambiar ícono de favorito
        val isFav = isFavoriteChecker(movie)
        holder.btnFavorite.setImageResource(
            if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )

        // Clicks en botones
        holder.btnFavorite.setOnClickListener { onFavoriteClick(movie) }
        holder.btnMoreInfo.setOnClickListener { onMoreInfoClick(movie) }
    }

    override fun getItemCount(): Int = movies.size
}
