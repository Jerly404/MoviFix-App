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

class MovieAdapter(
    private val movies: List<Movie>,
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imgThumbnail)     // ← corregido
        val title: TextView = itemView.findViewById(R.id.tvTitle)       // ← corregido
        // val year: TextView = itemView.findViewById(R.id.tvYear)       // ← eliminar o corregir
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        // holder.year.text = movie.year // ← quitar si no tienes `tvYear`
        Glide.with(holder.itemView.context)
            .load(movie.imageUrl)
            .into(holder.image)
    }

    override fun getItemCount(): Int = movies.size
}
